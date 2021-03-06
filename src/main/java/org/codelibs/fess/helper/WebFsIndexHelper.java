/*
 * Copyright 2012-2016 CodeLibs Project and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.codelibs.fess.helper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codelibs.core.lang.StringUtil;
import org.codelibs.core.misc.DynamicProperties;
import org.codelibs.fess.Constants;
import org.codelibs.fess.app.service.BoostDocumentRuleService;
import org.codelibs.fess.app.service.FailureUrlService;
import org.codelibs.fess.app.service.FileAuthenticationService;
import org.codelibs.fess.app.service.FileConfigService;
import org.codelibs.fess.app.service.WebConfigService;
import org.codelibs.fess.crawler.Crawler;
import org.codelibs.fess.crawler.CrawlerContext;
import org.codelibs.fess.crawler.CrawlerStatus;
import org.codelibs.fess.crawler.interval.FessIntervalController;
import org.codelibs.fess.crawler.service.impl.EsDataService;
import org.codelibs.fess.crawler.service.impl.EsUrlFilterService;
import org.codelibs.fess.crawler.service.impl.EsUrlQueueService;
import org.codelibs.fess.es.config.exentity.CrawlingConfig.ConfigName;
import org.codelibs.fess.es.config.exentity.FileConfig;
import org.codelibs.fess.es.config.exentity.WebConfig;
import org.codelibs.fess.indexer.IndexUpdater;
import org.codelibs.fess.mylasta.direction.FessConfig;
import org.codelibs.fess.util.ComponentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebFsIndexHelper implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(WebFsIndexHelper.class);

    @Resource
    protected DynamicProperties systemProperties;

    @Resource
    public WebConfigService webConfigService;

    @Resource
    protected FileConfigService fileConfigService;

    @Resource
    protected FileAuthenticationService fileAuthenticationService;

    @Resource
    public FailureUrlService failureUrlService;

    @Resource
    protected BoostDocumentRuleService boostDocumentRuleService;

    @Resource
    protected CrawlingConfigHelper crawlingConfigHelper;

    public long maxAccessCount = Long.MAX_VALUE;

    public long crawlingExecutionInterval = Constants.DEFAULT_CRAWLING_EXECUTION_INTERVAL;

    public int indexUpdaterPriority = Thread.MAX_PRIORITY;

    public int crawlerPriority = Thread.NORM_PRIORITY;

    private final List<Crawler> crawlerList = Collections.synchronizedList(new ArrayList<Crawler>());

    // needed?
    @Deprecated
    public void crawl(final String sessionId) {
        final List<WebConfig> webConfigList = webConfigService.getAllWebConfigList();
        final List<FileConfig> fileConfigList = fileConfigService.getAllFileConfigList();

        if (webConfigList.isEmpty() && fileConfigList.isEmpty()) {
            // nothing
            if (logger.isInfoEnabled()) {
                logger.info("No crawling target urls.");
            }
            return;
        }

        doCrawl(sessionId, webConfigList, fileConfigList);
    }

    public void crawl(final String sessionId, final List<String> webConfigIdList, final List<String> fileConfigIdList) {
        final boolean runAll = webConfigIdList == null && fileConfigIdList == null;
        final List<WebConfig> webConfigList;
        if (runAll || webConfigIdList != null) {
            webConfigList = webConfigService.getWebConfigListByIds(webConfigIdList);
        } else {
            webConfigList = Collections.emptyList();
        }
        final List<FileConfig> fileConfigList;
        if (runAll || fileConfigIdList != null) {
            fileConfigList = fileConfigService.getFileConfigListByIds(fileConfigIdList);
        } else {
            fileConfigList = Collections.emptyList();
        }

        if (webConfigList.isEmpty() && fileConfigList.isEmpty()) {
            // nothing
            if (logger.isInfoEnabled()) {
                logger.info("No crawling target urls.");
            }
            return;
        }

        doCrawl(sessionId, webConfigList, fileConfigList);
    }

    protected void doCrawl(final String sessionId, final List<WebConfig> webConfigList, final List<FileConfig> fileConfigList) {
        final int multiprocessCrawlingCount = ComponentUtil.getFessConfig().getCrawlingThreadCount();

        final SystemHelper systemHelper = ComponentUtil.getSystemHelper();
        final FessConfig fessConfig = ComponentUtil.getFessConfig();

        final long startTime = System.currentTimeMillis();

        final List<String> sessionIdList = new ArrayList<>();
        crawlerList.clear();
        final List<String> crawlerStatusList = new ArrayList<>();
        // Web
        for (final WebConfig webConfig : webConfigList) {
            final String sid = crawlingConfigHelper.store(sessionId, webConfig);

            // create crawler
            final Crawler crawler = ComponentUtil.getComponent(Crawler.class);
            crawler.setSessionId(sid);
            sessionIdList.add(sid);

            final String urlsStr = webConfig.getUrls();
            if (StringUtil.isBlank(urlsStr)) {
                logger.warn("No target urls. Skipped");
                break;
            }

            // interval time
            final int intervalTime =
                    webConfig.getIntervalTime() != null ? webConfig.getIntervalTime() : Constants.DEFAULT_INTERVAL_TIME_FOR_WEB;
            ((FessIntervalController) crawler.getIntervalController()).setDelayMillisForWaitingNewUrl(intervalTime);

            final String includedUrlsStr = webConfig.getIncludedUrls() != null ? webConfig.getIncludedUrls() : StringUtil.EMPTY;
            final String excludedUrlsStr = webConfig.getExcludedUrls() != null ? webConfig.getExcludedUrls() : StringUtil.EMPTY;

            // num of threads
            final CrawlerContext crawlerContext = crawler.getCrawlerContext();
            final int numOfThread =
                    webConfig.getNumOfThread() != null ? webConfig.getNumOfThread() : Constants.DEFAULT_NUM_OF_THREAD_FOR_WEB;
            crawlerContext.setNumOfThread(numOfThread);

            // depth
            final int depth = webConfig.getDepth() != null ? webConfig.getDepth() : -1;
            crawlerContext.setMaxDepth(depth);

            // max count
            final long maxCount = webConfig.getMaxAccessCount() != null ? webConfig.getMaxAccessCount() : maxAccessCount;
            crawlerContext.setMaxAccessCount(maxCount);

            webConfig.initializeClientFactory(crawler.getClientFactory());
            final Map<String, String> configParamMap = webConfig.getConfigParameterMap(ConfigName.CONFIG);

            if (Constants.TRUE.equalsIgnoreCase(configParamMap.get(Constants.CONFIG_CLEANUP_ALL))) {
                deleteCrawlData(sid);
            } else if (Constants.TRUE.equalsIgnoreCase(configParamMap.get(Constants.CONFIG_CLEANUP_FILTERS))) {
                final EsUrlFilterService urlFilterService = ComponentUtil.getComponent(EsUrlFilterService.class);
                try {
                    urlFilterService.delete(sid);
                } catch (final Exception e) {
                    logger.warn("Failed to delete url filters for " + sid);
                }
            }

            // set urls
            final String[] urls = urlsStr.split("[\r\n]");
            for (final String u : urls) {
                if (StringUtil.isNotBlank(u)) {
                    final String urlValue = u.trim();
                    if (!urlValue.startsWith("#") && fessConfig.isValidCrawlerWebProtocol(u)) {
                        crawler.addUrl(urlValue);
                        if (logger.isInfoEnabled()) {
                            logger.info("Target URL: " + urlValue);
                        }
                    }
                }
            }

            // set included urls
            final String[] includedUrls = includedUrlsStr.split("[\r\n]");
            for (final String u : includedUrls) {
                if (StringUtil.isNotBlank(u)) {
                    final String urlValue = u.trim();
                    if (!urlValue.startsWith("#")) {
                        crawler.addIncludeFilter(urlValue);
                        if (logger.isInfoEnabled()) {
                            logger.info("Included URL: " + urlValue);
                        }
                    }
                }
            }

            // set excluded urls
            final String[] excludedUrls = excludedUrlsStr.split("[\r\n]");
            for (final String u : excludedUrls) {
                if (StringUtil.isNotBlank(u)) {
                    final String urlValue = u.trim();
                    if (!urlValue.startsWith("#")) {
                        crawler.addExcludeFilter(urlValue);
                        if (logger.isInfoEnabled()) {
                            logger.info("Excluded URL: " + urlValue);
                        }
                    }
                }
            }

            // failure url
            if (!Constants.TRUE.equalsIgnoreCase(configParamMap.get(Constants.CONFIG_IGNORE_FAILURE_URLS))) {
                final List<String> excludedUrlList = failureUrlService.getExcludedUrlList(webConfig.getConfigId());
                for (final String u : excludedUrlList) {
                    if (StringUtil.isNotBlank(u)) {
                        final String urlValue = u.trim();
                        crawler.addExcludeFilter(urlValue);
                        if (logger.isInfoEnabled()) {
                            logger.info("Excluded URL from failures: " + urlValue);
                        }
                    }
                }
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Crawling " + urlsStr);
            }

            crawler.setBackground(true);
            crawler.setThreadPriority(crawlerPriority);

            crawlerList.add(crawler);
            crawlerStatusList.add(Constants.READY);
        }

        // File
        for (final FileConfig fileConfig : fileConfigList) {
            final String sid = crawlingConfigHelper.store(sessionId, fileConfig);

            // create crawler
            final Crawler crawler = ComponentUtil.getComponent(Crawler.class);
            crawler.setSessionId(sid);
            sessionIdList.add(sid);

            final String pathsStr = fileConfig.getPaths();
            if (StringUtil.isBlank(pathsStr)) {
                logger.warn("No target uris. Skipped");
                break;
            }

            final int intervalTime =
                    fileConfig.getIntervalTime() != null ? fileConfig.getIntervalTime() : Constants.DEFAULT_INTERVAL_TIME_FOR_FS;
            ((FessIntervalController) crawler.getIntervalController()).setDelayMillisForWaitingNewUrl(intervalTime);

            final String includedPathsStr = fileConfig.getIncludedPaths() != null ? fileConfig.getIncludedPaths() : StringUtil.EMPTY;
            final String excludedPathsStr = fileConfig.getExcludedPaths() != null ? fileConfig.getExcludedPaths() : StringUtil.EMPTY;

            // num of threads
            final CrawlerContext crawlerContext = crawler.getCrawlerContext();
            final int numOfThread =
                    fileConfig.getNumOfThread() != null ? fileConfig.getNumOfThread() : Constants.DEFAULT_NUM_OF_THREAD_FOR_FS;
            crawlerContext.setNumOfThread(numOfThread);

            // depth
            final int depth = fileConfig.getDepth() != null ? fileConfig.getDepth() : -1;
            crawlerContext.setMaxDepth(depth);

            // max count
            final long maxCount = fileConfig.getMaxAccessCount() != null ? fileConfig.getMaxAccessCount() : maxAccessCount;
            crawlerContext.setMaxAccessCount(maxCount);

            fileConfig.initializeClientFactory(crawler.getClientFactory());
            final Map<String, String> configParamMap = fileConfig.getConfigParameterMap(ConfigName.CONFIG);

            if (Constants.TRUE.equalsIgnoreCase(configParamMap.get(Constants.CONFIG_CLEANUP_ALL))) {
                deleteCrawlData(sid);
            } else if (Constants.TRUE.equalsIgnoreCase(configParamMap.get(Constants.CONFIG_CLEANUP_FILTERS))) {
                final EsUrlFilterService urlFilterService = ComponentUtil.getComponent(EsUrlFilterService.class);
                try {
                    urlFilterService.delete(sid);
                } catch (final Exception e) {
                    logger.warn("Failed to delete url filters for " + sid);
                }
            }

            // set paths
            final String[] paths = pathsStr.split("[\r\n]");
            for (String u : paths) {
                if (StringUtil.isNotBlank(u)) {
                    u = u.trim();
                    if (!u.startsWith("#")) {
                        if (!fessConfig.isValidCrawlerFileProtocol(u)) {
                            if (u.startsWith("/")) {
                                u = "file:" + u;
                            } else {
                                u = "file:/" + u;
                            }
                        }
                        crawler.addUrl(u);
                        if (logger.isInfoEnabled()) {
                            logger.info("Target Path: " + u);
                        }
                    }
                }
            }

            // set included paths
            boolean urlEncodeDisabled = false;
            final String[] includedPaths = includedPathsStr.split("[\r\n]");
            for (final String u : includedPaths) {
                if (StringUtil.isNotBlank(u)) {
                    final String line = u.trim();
                    if (!line.startsWith("#")) {
                        final String urlValue;
                        if (urlEncodeDisabled) {
                            urlValue = line;
                            urlEncodeDisabled = false;
                        } else {
                            urlValue = systemHelper.encodeUrlFilter(line);
                        }
                        crawler.addIncludeFilter(urlValue);
                        if (logger.isInfoEnabled()) {
                            logger.info("Included Path: " + urlValue);
                        }
                    } else if (line.startsWith("#DISABLE_URL_ENCODE")) {
                        urlEncodeDisabled = true;
                    }
                }
            }

            // set excluded paths
            urlEncodeDisabled = false;
            final String[] excludedPaths = excludedPathsStr.split("[\r\n]");
            for (final String u : excludedPaths) {
                if (StringUtil.isNotBlank(u)) {
                    final String line = u.trim();
                    if (!line.startsWith("#")) {
                        final String urlValue;
                        if (urlEncodeDisabled) {
                            urlValue = line;
                            urlEncodeDisabled = false;
                        } else {
                            urlValue = systemHelper.encodeUrlFilter(line);
                        }
                        crawler.addExcludeFilter(urlValue);
                        if (logger.isInfoEnabled()) {
                            logger.info("Excluded Path: " + urlValue);
                        }
                    } else if (line.startsWith("#DISABLE_URL_ENCODE")) {
                        urlEncodeDisabled = true;
                    }
                }
            }

            // failure url
            if (!Constants.TRUE.equalsIgnoreCase(configParamMap.get(Constants.CONFIG_IGNORE_FAILURE_URLS))) {
                final List<String> excludedUrlList = failureUrlService.getExcludedUrlList(fileConfig.getConfigId());
                if (excludedUrlList != null) {
                    for (final String u : excludedUrlList) {
                        if (StringUtil.isNotBlank(u)) {
                            final String urlValue = u.trim();
                            crawler.addExcludeFilter(urlValue);
                            if (logger.isInfoEnabled()) {
                                logger.info("Excluded Path from failures: " + urlValue);
                            }
                        }
                    }
                }
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Crawling " + pathsStr);
            }

            crawler.setBackground(true);
            crawler.setThreadPriority(crawlerPriority);

            crawlerList.add(crawler);
            crawlerStatusList.add(Constants.READY);
        }

        // run index update
        final IndexUpdater indexUpdater = ComponentUtil.getIndexUpdater();
        indexUpdater.setName("IndexUpdater");
        indexUpdater.setPriority(indexUpdaterPriority);
        indexUpdater.setSessionIdList(sessionIdList);
        indexUpdater.setDaemon(true);
        indexUpdater.setCrawlerList(crawlerList);
        boostDocumentRuleService.getAvailableBoostDocumentRuleList().forEach(rule -> {
            indexUpdater.addDocBoostMatcher(new org.codelibs.fess.indexer.DocBoostMatcher(rule));
        });
        indexUpdater.start();

        int startedCrawlerNum = 0;
        int activeCrawlerNum = 0;
        while (startedCrawlerNum < crawlerList.size()) {
            // Force to stop crawl
            if (systemHelper.isForceStop()) {
                for (final Crawler crawler : crawlerList) {
                    crawler.stop();
                }
                break;
            }

            if (activeCrawlerNum < multiprocessCrawlingCount) {
                // start crawling
                crawlerList.get(startedCrawlerNum).execute();
                crawlerStatusList.set(startedCrawlerNum, Constants.RUNNING);
                startedCrawlerNum++;
                activeCrawlerNum++;
                try {
                    Thread.sleep(crawlingExecutionInterval);
                } catch (final InterruptedException e) {
                    // NOP
                }
                continue;
            }

            // check status
            for (int i = 0; i < startedCrawlerNum; i++) {
                if (crawlerList.get(i).getCrawlerContext().getStatus() == CrawlerStatus.DONE
                        && crawlerStatusList.get(i).equals(Constants.RUNNING)) {
                    crawlerList.get(i).awaitTermination();
                    crawlerStatusList.set(i, Constants.DONE);
                    final String sid = crawlerList.get(i).getCrawlerContext().getSessionId();
                    indexUpdater.addFinishedSessionId(sid);
                    activeCrawlerNum--;
                }
            }
            try {
                Thread.sleep(crawlingExecutionInterval);
            } catch (final InterruptedException e) {
                // NOP
            }
        }

        boolean finishedAll = false;
        while (!finishedAll) {
            finishedAll = true;
            for (int i = 0; i < crawlerList.size(); i++) {
                crawlerList.get(i).awaitTermination(crawlingExecutionInterval);
                if (crawlerList.get(i).getCrawlerContext().getStatus() == CrawlerStatus.DONE
                        && !crawlerStatusList.get(i).equals(Constants.DONE)) {
                    crawlerStatusList.set(i, Constants.DONE);
                    final String sid = crawlerList.get(i).getCrawlerContext().getSessionId();
                    indexUpdater.addFinishedSessionId(sid);
                }
                if (!crawlerStatusList.get(i).equals(Constants.DONE)) {
                    finishedAll = false;
                }
            }
        }
        crawlerList.clear();
        crawlerStatusList.clear();

        // put cralwing info
        final CrawlingInfoHelper crawlingInfoHelper = ComponentUtil.getCrawlingInfoHelper();

        final long execTime = System.currentTimeMillis() - startTime;
        crawlingInfoHelper.putToInfoMap(Constants.WEB_FS_CRAWLING_EXEC_TIME, Long.toString(execTime));
        if (logger.isInfoEnabled()) {
            logger.info("[EXEC TIME] crawling time: " + execTime + "ms");
        }

        indexUpdater.setFinishCrawling(true);
        try {
            indexUpdater.join();
        } catch (final InterruptedException e) {
            logger.warn("Interrupted index update.", e);
        }

        crawlingInfoHelper.putToInfoMap(Constants.WEB_FS_INDEX_EXEC_TIME, Long.toString(indexUpdater.getExecuteTime()));
        crawlingInfoHelper.putToInfoMap(Constants.WEB_FS_INDEX_SIZE, Long.toString(indexUpdater.getDocumentSize()));

        if (systemHelper.isForceStop()) {
            return;
        }

        for (final String sid : sessionIdList) {
            // remove config
            crawlingConfigHelper.remove(sid);
            deleteCrawlData(sid);
        }
    }

    protected void deleteCrawlData(final String sid) {
        final EsUrlFilterService urlFilterService = ComponentUtil.getComponent(EsUrlFilterService.class);
        final EsUrlQueueService urlQueueService = ComponentUtil.getComponent(EsUrlQueueService.class);
        final EsDataService dataService = ComponentUtil.getComponent(EsDataService.class);

        try {
            // clear url filter
            urlFilterService.delete(sid);
        } catch (final Exception e) {
            logger.warn("Failed to delete UrlFilter for " + sid, e);
        }

        try {
            // clear queue
            urlQueueService.clearCache();
            urlQueueService.delete(sid);
        } catch (final Exception e) {
            logger.warn("Failed to delete UrlQueue for " + sid, e);
        }

        try {
            // clear
            dataService.delete(sid);
        } catch (final Exception e) {
            logger.warn("Failed to delete AccessResult for " + sid, e);
        }
    }

}

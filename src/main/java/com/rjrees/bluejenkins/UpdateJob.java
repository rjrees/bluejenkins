package com.rjrees.bluejenkins;

import com.rjrees.bluejenkins.jenkins.BuildStatus;
import com.rjrees.bluejenkins.jenkins.JenkinsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Gordon on 03/12/2014.
 */
@Component
public class UpdateJob {

    private static Logger LOGGER = LoggerFactory.getLogger(UpdateJob.class);

    // Connection to the Jenkins Manager
    private JenkinsManager jenkinsMgr;

    @Value("${jenkins.url}")
    private String jenkinsUrl;

    @Value("${jenkins.build1}")
    private String build1;

    @Value("${jenkins.build2}")
    private String build2;

    @Autowired
    private UpdateJob(JenkinsManager jenkins) {
        this.jenkinsMgr = jenkins;
        LOGGER.info("Using Jenkins server at : " + jenkinsUrl);
    }

    /**
     * This is the task performed by the scheduler to update the lights
     */
    public void updateLights() {

        LOGGER.info("Updating lights! " + jenkinsUrl);

        jenkinsMgr.connect(jenkinsUrl, null, null);
        jenkinsMgr.refresh();

        BuildStatus status1 = jenkinsMgr.getJobStatusByName(build1);
        BuildStatus status2 = jenkinsMgr.getJobStatusByName(build2);

        LOGGER.info(status1.toString());
        LOGGER.info(status2.toString());

    }
}

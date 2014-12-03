package com.rjrees.bluejenkins.jenkins;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * Created by Gordon on 03/12/2014.
 */
@Component
public class JenkinsManager {
    private static Logger LOGGER = LoggerFactory.getLogger(JenkinsManager.class);

    private JenkinsServer jenkins;
    private Map<String, Job> jobs;

    public JenkinsManager() {
        LOGGER.info("Creating JenkinsManager");
    }

    /**
     * Setup the connection parameters
     * @param url
     * @param username
     * @param password
     */
    public void connect(final String url , final String username , final String password) {
        try {
            if( null == username) {
                jenkins = new JenkinsServer(new URI(url));
            } else {
                jenkins = new JenkinsServer(new URI(url), username , password);
            }
        }catch (Exception ex) {
            LOGGER.error( "Failed to connect to Jenkins", ex );
        }
    }

    /**
     * Update the status of the jobs from the server
     */
    public void refresh() {
        LOGGER.debug("Refresh jobs ");
        try {
            jobs = jenkins.getJobs();
        } catch (IOException e) {
            LOGGER.error("Failed to retrieve jobs");
            jobs = null;
        }
    }
    /**
     *
     * @param name
     */
    public  BuildStatus getJobStatusByName(final String name) {
        if( null == jenkins) {
            LOGGER.error("Jenkins server NOT connected");
            return null;
        }

        if( null == jobs ) {
            LOGGER.warn("No JOBS ?");
            return null;
        }

        try {
            LOGGER.debug("Getting : " + name);
            JobWithDetails job = jobs.get(name).details();

            //JobWithDetails job = jenkins.getJob(name);
            Build lastBuild = job.getLastBuild();
            BuildWithDetails details = lastBuild.details();

            Build completedBuild = job.getLastCompletedBuild();
            BuildWithDetails completedDetails = completedBuild.details();

            // Create the return status
            BuildStatus status = new BuildStatus( name );
            status.setBuilding( details.isBuilding());
            status.setLastCompletedResult( completedDetails.getResult().toString() );

            return status;
        } catch (IOException ex) {
            LOGGER.error("Failed to get job :" + name , ex);
        }

        return null;
    }

    /**
     * Debug routing to list the jobs defined on the server
     */
    public void listJobs() {
        try {
            Map<String, Job> jobs = jenkins.getJobs();

            for (String job : jobs.keySet()) {
                LOGGER.info(job);
            }
        } catch (Exception ex) {
            LOGGER.error("Failed to list jobs" , ex);
        }
    }
}


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import JobStatus.Status;


public class JenkinsClient {

	
	HttpClient client = new HttpClient();
	String jenkinsBaseURL;
	
	public JenkinsClient(String hudsonBaseURL) {
		this.jenkinsBaseURL = hudsonBaseURL;
	}

	public void authenticate(String username, String password) {
		try {			
			PostMethod postMethodAuth = new PostMethod(jenkinsBaseURL + "/j_acegi_security_check");
			NameValuePair[] postData = new NameValuePair[3];
			postData[0] = new NameValuePair("j_username", username);
			postData[1] = new NameValuePair("j_password", password);
			postData[2] = new NameValuePair("Login", "login");
			postMethodAuth.addParameters(postData);
			int status = client.executeMethod(postMethodAuth);
			if (status != 200) {
				throw new RuntimeException("");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public boolean isHudsonURL () throws IOException, HttpException {
		GetMethod getMethod = new GetMethod(jenkinsBaseURL);
		int status = client.executeMethod(getMethod);
		if (status != 200) {
			throw new RuntimeException("");
		}
		return getMethod.getResponseHeader("X-Hudson")!=null;
	}

	public void createJob(String jobName, InputStream configFile) throws IOException, HttpException {
		PostMethod postMethod = new PostMethod(jenkinsBaseURL+ "/createItem?name=" + jobName);
		postMethod.setRequestHeader("Content-type","application/xml; charset=ISO-8859-1");
		postMethod.setRequestEntity(new InputStreamRequestEntity(configFile));
		postMethod.setDoAuthentication(true);
		try {
			int status = client.executeMethod(postMethod);
			if (status != 200) {
				throw new RuntimeException("");
			}
		} finally {
			postMethod.releaseConnection();
		}
	}
	
	public void updateJob(String jobName, InputStream updateFile) throws IOException, HttpException {
		PostMethod postMethod = new PostMethod(jenkinsBaseURL + "/job/"+ jobName + "/config.xml");
		postMethod.setRequestHeader("Content-type","text/xml; charset=ISO-8859-1");
		postMethod.setRequestEntity(new InputStreamRequestEntity(updateFile));
		try {
			int status = client.executeMethod(postMethod);
			if (status != 200) {
				throw new RuntimeException("");
			}
		} finally {
			postMethod.releaseConnection();
		}
	}

	public void copyJob(String originJobName, String newJobName) throws IOException, HttpException {
		PostMethod postMethod = new PostMethod(jenkinsBaseURL + "/createItem");
		NameValuePair n1 = new NameValuePair("name", newJobName);
		NameValuePair n2 = new NameValuePair("mode", "copy");
		NameValuePair n3 = new NameValuePair("from", originJobName);
		postMethod.setQueryString(new NameValuePair[] { n1, n2, n3 });
		postMethod.setRequestHeader("Content-type","text/plain; charset=ISO-8859-1");
		int status = client.executeMethod(postMethod);
		if (status != 200) {
			throw new RuntimeException("");
		}
	}

	public void deleteJob(String jobName) throws IOException, HttpException {
		PostMethod postMethod = new PostMethod(jenkinsBaseURL + "/job/"+ jobName + "/doDelete");
		int status = client.executeMethod(postMethod);
		if (status != 200) {
			throw new RuntimeException("");
		}
	}
	
	public void enableJob(String jobName) throws IOException, HttpException {
		PostMethod postMethod = new PostMethod(jenkinsBaseURL + "/job/"+ jobName + "/enable");
		int status = client.executeMethod(postMethod);
		if (status != 200) {
			throw new RuntimeException("");
		}
	}
	 
	public void disableJob(String jobName) throws IOException, HttpException {
		PostMethod postMethod = new PostMethod(jenkinsBaseURL + "/job/"+ jobName + "/disable");
		int status = client.executeMethod(postMethod);
		if (status != 200) {
			throw new RuntimeException("");
		}
	}

	public InputStream readJob(HttpClient client, String hudsonBaseURL, String jobName) throws IOException, HttpException {
		GetMethod getMethod = new GetMethod(hudsonBaseURL + "/job/" + jobName+ "/config.xml");
		int status = client.executeMethod(getMethod);
		if (status != 200) {
			throw new RuntimeException("");
		}
		byte[] response = getMethod.getResponseBody();
		return new ByteArrayInputStream(response);
	}
	
	public void launchBuild(String jobName) throws IOException, HttpException {
		GetMethod getMethod = new GetMethod(jenkinsBaseURL + "/job/"+ jobName+ "/build");
		int status = client.executeMethod(getMethod);
		if (status != 200) {
			throw new RuntimeException("Impossible to launch build");
		}
	}
	
	public void launchBuildWithParameters(String jobName, Map<String, String> parameters) throws IOException, HttpException {
		GetMethod getMethod = new GetMethod(jenkinsBaseURL + "/job/"+ jobName+ "/buildWithParameters?param1=value1");
		int status = client.executeMethod(getMethod);
		if (status != 200) {
			throw new RuntimeException("Impossible to launch build");
		}
	}
	
	public void lastSuccessfullBuild(String jobName) throws IOException, HttpException {
		GetMethod getMethod = new GetMethod(jenkinsBaseURL + "/job/"+ jobName+ "/lastSuccessfulBuild/buildTimestamp?format=dd/MM/yyyy");
		try {
			int status = client.executeMethod(getMethod);
			System.out.println(status + "\n"+ getMethod.getResponseBodyAsString());
		} finally {
			getMethod.releaseConnection();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<JobStatus> readJobStatus() throws IOException, HttpException,DocumentException {
		List<JobStatus> jobStatus = new ArrayList<JobStatus>();
		URL url = new URL(jenkinsBaseURL + "/api/xml");
		Document dom = new SAXReader().read(url);
		for (Element job : (List<Element>) dom.getRootElement().elements("job")) {
			jobStatus.add(new JobStatus(job.elementText("name"), Status.create(job.elementText("color"))));
		}
		return jobStatus;
	}
}

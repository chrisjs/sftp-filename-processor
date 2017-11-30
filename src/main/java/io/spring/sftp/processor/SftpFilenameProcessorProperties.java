package io.spring.sftp.processor;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties class for SftpFilenameProcessor.
 *
 * @author Chris Schaefer
 */
@ConfigurationProperties
public class SftpFilenameProcessorProperties {
	private static final Integer DEFAULT_SFTP_PORT = 22;
	private static final String DEFAULT_LOCAL_FILE_PATH_JOB_PARAM_NAME = "localFilePath";
	private static final String DEFAULT_REMOTE_FILE_PATH_JOB_PARAM_NAME = "remoteFilePath";

	/**
	 * The URI of the artifact to be applied to the TaskLaunchRequest.
	 */
	private String uri;

	/**
	 * The datasource url to be applied to the TaskLaunchRequest.
	 */
	private String dataSourceUrl;

	/**
	 * The datasource user name to be applied to the TaskLaunchRequest.
	 */
	private String dataSourceUserName;

	/**
	 * Comma delimited list of deployment properties to be applied to the
	 * TaskLaunchRequest.
	 */
	private String deploymentProperties;

	/**
	 * Value to use as the remote file job parameter name.
	 */
	private String remoteFilePathJobParameterName = DEFAULT_REMOTE_FILE_PATH_JOB_PARAM_NAME;

	/**
	 * Value to use as the local file job parameter name.
	 */
	private String localFilePathJobParameterName = DEFAULT_LOCAL_FILE_PATH_JOB_PARAM_NAME;

	/**
	 * The file path to use as the local file job paramter value.
	 */
	private String localFilePathJobParameterValue;

	/**
	 * The SFTP host to fetch files from.
	 */
	private String sftpHost;

	/**
	 * The (optional) SFTP host port, defaults to 22.
	 */
	private Integer sftpPort = DEFAULT_SFTP_PORT;

	/**
	 * The SFTP username.
	 */
	private String sftpUsername;

	/**
	 * The SFTP password.
	 */
	private String sftpPassword;

	@NotBlank
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@NotBlank
	public String getDataSourceUrl() {
		return dataSourceUrl;
	}

	public void setDataSourceUrl(String dataSourceUrl) {
		this.dataSourceUrl = dataSourceUrl;
	}

	@NotBlank
	public String getDataSourceUserName() {
		return dataSourceUserName;
	}

	public void setDataSourceUserName(String dataSourceUserName) {
		this.dataSourceUserName = dataSourceUserName;
	}

	public String getDeploymentProperties() {
		return deploymentProperties;
	}

	public void setDeploymentProperties(String deploymentProperties) {
		this.deploymentProperties = deploymentProperties;
	}

	public String getRemoteFilePathJobParameterName() {
		return remoteFilePathJobParameterName;
	}

	public void setRemoteFilePathJobParameterName(String remoteFilePathJobParameterName) {
		this.remoteFilePathJobParameterName = remoteFilePathJobParameterName;
	}

	public String getLocalFilePathJobParameterName() {
		return localFilePathJobParameterName;
	}

	public void setLocalFilePathJobParameterName(String localFilePathJobParameterName) {
		this.localFilePathJobParameterName = localFilePathJobParameterName;
	}

	@NotBlank
	public String getLocalFilePathJobParameterValue() {
		return localFilePathJobParameterValue;
	}

	public void setLocalFilePathJobParameterValue(String localFilePathJobParameterValue) {
		this.localFilePathJobParameterValue = localFilePathJobParameterValue;
	}

	@NotBlank
	public String getSftpHost() {
		return sftpHost;
	}

	public void setSftpHost(String sftpHost) {
		this.sftpHost = sftpHost;
	}

	@Range(min = 0, max = 65535)
	public Integer getSftpPort() {
		return sftpPort;
	}

	public void setSftpPort(Integer sftpPort) {
		this.sftpPort = sftpPort;
	}

	@NotBlank
	public String getSftpUsername() {
		return sftpUsername;
	}

	public void setSftpUsername(String sftpUsername) {
		this.sftpUsername = sftpUsername;
	}

	@NotBlank
	public String getSftpPassword() {
		return sftpPassword;
	}

	public void setSftpPassword(String sftpPassword) {
		this.sftpPassword = sftpPassword;
	}
}

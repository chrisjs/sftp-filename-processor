package io.spring.sftp.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.task.launcher.TaskLaunchRequest;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;

/**
 * Transformer class that creates a TaskLaunchRequest with the provided
 * properties and file path info from an SFTP source message.
 *
 * @author Chris Schaefer
 */
@EnableBinding(Processor.class)
@EnableConfigurationProperties(SftpFilenameProcessorProperties.class)
public class SftpFilenameProcessor {
	private static final String FILE_MESSAGE_HEADER = "file_remoteFile";
	private static final String REMOTE_DIRECTORY_HEADER = "file_remoteDirectory";
	private static final String SFTP_HOST_PROPERTY_KEY = "sftp_host";
	private static final String SFTP_PORT_PROPERTY_KEY = "sftp_port";
	private static final String SFTP_USERNAME_PROPERTY_KEY = "sftp_username";
	private static final String SFTP_PASSWORD_PROPERTY_KEY = "sftp_password";
	private static final String DATASOURCE_URL_PROPERTY_KEY = "spring_datasource_url";
	private static final String DATASOURCE_USERNAME_PROPERTY_KEY = "spring_datasource_username";

	private final SftpFilenameProcessorProperties processorProperties;

	@Autowired
	public SftpFilenameProcessor(final SftpFilenameProcessorProperties processorProperties) {
		this.processorProperties = processorProperties;
	}

	@Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
	public Object transform(Message<?> message) {
		checkPreconditions(message);

		return new TaskLaunchRequest(processorProperties.getUri(), getCommandLineArgs(message),
								getDeploymentProperties(), null, null);
	}

	private void checkPreconditions(Message message) {
		Assert.notNull(message, "Message to transform is null");
		Assert.notNull(message.getHeaders(), "Message headers are null");
		Assert.notNull(message.getHeaders().get(FILE_MESSAGE_HEADER), "Remote filename message header is null");
		Assert.notNull(message.getHeaders().get(REMOTE_DIRECTORY_HEADER), "Remote file directory message header is null");
	}

	private Map<String, String> getDeploymentProperties() {
		Map<String, String> properties = new HashMap<String, String>();
		properties.put(DATASOURCE_URL_PROPERTY_KEY, processorProperties.getDataSourceUrl());
		properties.put(DATASOURCE_USERNAME_PROPERTY_KEY, processorProperties.getDataSourceUserName());
		properties.put(SFTP_HOST_PROPERTY_KEY, processorProperties.getSftpHost());
		properties.put(SFTP_USERNAME_PROPERTY_KEY, processorProperties.getSftpUsername());
		properties.put(SFTP_PASSWORD_PROPERTY_KEY, processorProperties.getSftpPassword());

		Integer sftpPort = processorProperties.getSftpPort();
		properties.put(SFTP_PORT_PROPERTY_KEY, sftpPort != null ? sftpPort.toString() : null);

		return properties;
	}

	private List<String> getCommandLineArgs(Message message) {
		String filename = (String) message.getHeaders().get(FILE_MESSAGE_HEADER);
		String remoteDirectory = (String) message.getHeaders().get(REMOTE_DIRECTORY_HEADER);
		String localFilePathJobParamterValue = processorProperties.getLocalFilePathJobParameterValue().trim();

		String remoteFilePath = buildFilePath(remoteDirectory, filename);
		String localFilePath = buildFilePath(localFilePathJobParamterValue, filename);
		String localFilePathJobParameterName = processorProperties.getLocalFilePathJobParameterName().trim();
		String remoteFilePathJobParameterName = processorProperties.getRemoteFilePathJobParameterName().trim();

		List<String> commandLineArgs = new ArrayList<String>();
		commandLineArgs.add(remoteFilePathJobParameterName + "=" + remoteFilePath);
		commandLineArgs.add(localFilePathJobParameterName + "=" + localFilePath);

		return commandLineArgs;
	}

	private String buildFilePath(String directory, String filename) {
		StringBuilder sftpUri = new StringBuilder()
			.append(directory);

		if (!directory.endsWith("/")) {
			sftpUri.append("/");
		}

		sftpUri.append(filename);

		return sftpUri.toString();
	}
}

# SFTP to Batch Job POC -  Use case #1

* SftpStreamingMessageSource (`--stream` from built in SFTP source) -> transformer -> task-launcher -> batch job
* Tested with kafka binder only, local dataflow and local SFTP
* POC code, handles specific use case

# Pre-req's:
* SFTP server
* Local DataFlow server and shell
* Built file ingest batch job jar from [Batch Ingest Sample](https://github.com/spring-cloud/spring-cloud-dataflow-samples/tree/master/batch/file-ingest)

# Build processor
```
$ mvn clean package
```

# Init apps if needed

dataflow:> app import --uri http://bit.ly/Bacon-RELEASE-stream-applications-kafka-10-maven

# Register processor

dataflow:> app register --name filenameTransformer --type processor --uri file:////path/to/sftp-filename-processor-1.0.0.jar

# Create inbound SFTP stream

dataflow:> stream create inboundSftp --definition "sftp --username=user --password=pass --host=127.0.0.1 --port=6666 --allow-unknown-keys=true --stream=true --remote-dir=/remote/dir | filenameTransformer --uri=file:////path/to/ingest-X.X.X.jar --data-source-url=jdbc:h2:tcp://localhost:19092/mem:dataflow --data-source-user-name=sa --sftp-username=user --sftp-password=pass --sftp-host=127.0.0.1 --sftp-port=6666 --remote-file-path-job-parameter-name=remoteFilePath --local-file-path-job-parameter-name=localFilePath --local-file-path-job-parameter-value=/local/path/ > :filesAvailable"

# Deploy stream enabling message header passthrough

dataflow:> stream deploy inboundSftp --properties "app.sftp.spring.cloud.stream.kafka.binder.headers=file_remoteFile,file_remoteDirectory"

# Create ingest job stream

dataflow:> stream create ingestJob --definition ":filesAvailable > task-launcher-local" --deploy

* Copy data to SFTP location for retrieval

# Clean up

dataflow:> app unregister --name filenameTransformer --type processor

dataflow:> stream destroy inboundSftp

dataflow:> stream destroy ingestJob


package me.philcali.s3.maven.lifecycle;

import java.io.File;
import java.util.StringJoiner;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Mojo(name = "upload", defaultPhase = LifecyclePhase.PACKAGE)
public class UploadArtifactMojo extends AbstractMojo {
    @Parameter(property = "upload.bucket", required = true)
    private String bucket;

    @Parameter(property = "upload.prefix", defaultValue = "projects/${project.name}")
    private String prefix;

    @Parameter(property = "upload.region", required = true)
    private String region;

    @Parameter(property = "upload.jarFile", defaultValue = "${project.build.directory}/${project.name}-${project.version}.jar")
    private String jarFile;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Creating s3 client targeting region " + region);
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();

        getLog().info("Checking bucket availability " + bucket);
        if (!s3.doesBucketExistV2(bucket)) {
            getLog().info("Does not exists... creating it.");
            s3.createBucket(bucket);
        }

        final File artifact = new File(jarFile);
        final String s3Key = new StringJoiner("/")
                .add(prefix)
                .add(artifact.getName())
                .toString();
        getLog().info("Uploading " + jarFile + " to " + s3Key);
        s3.putObject(bucket, s3Key, artifact);
    }

}

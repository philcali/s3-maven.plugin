# s3-maven-plugin

Dead simple maven plugin to upload an artifact to s3.

## Does it make an s3 bucket a maven repo?

No. That's far too complicated for what this things is trying to accomplish.
If you want something like that, [let me google that for you][1].
I was thoroughly disappointed with first page results for simply jar's (ie: executables)
on s3. That's all this thing does.

[1]: https://www.google.com/search?q=setup+a+maven+repo+on+s3&rlz=1CAZZAD_enUS804&oq=setup+a+maven+repo+on+s3&aqs=chrome..69i57.247j0j9&sourceid=chrome&ie=UTF-8

## Usage

```
    <build>
      <plugins>
        <plugin>
  			<groupId>me.philcali</groupId>
  			<artifactId>s3-maven-plugin</artifactId>
  			<version>0.0.1-SNAPSHOT</version>
  			<configuration>
  			   <bucket>${your.s3.bucket}</bucket>
  			   <prefix>projects/${project.parent.artifactId}</prefix>
  			   <region>${aws.region}</region>
  			</configuration>
  			<executions>
  				<execution>
  					<phase>package</phase>
  					<goals>
  						<goal>upload</goal>
  					</goals>
  				</execution>
  			</executions>
  		</plugin>
     </plugins>
  </build>
```
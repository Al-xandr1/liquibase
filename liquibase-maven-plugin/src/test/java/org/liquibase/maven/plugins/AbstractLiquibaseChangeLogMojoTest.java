package org.liquibase.maven.plugins;

import liquibase.resource.ResourceAccessor;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

public class AbstractLiquibaseChangeLogMojoTest {


    @Test
    public void validateGetResourceAccessorDoesNotGeneratedTwoFileAccessorsWhenChangeLogDirectoryIsSet() throws MojoFailureException, IOException {
        //GIVEN
        AbstractLiquibaseChangeLogMojo mojo = new LiquibaseChangeLogMojoTest();
        mojo.project = mock(MavenProject.class);
        when(mojo.project.getBasedir()).thenReturn(new File(System.getProperty("user.dir")));
        mojo.changeLogDirectory = System.getProperty("user.dir");
        mojo.searchPath = null;
        //WHEN
        ResourceAccessor changeLogAccessor = mojo.getResourceAccessor(mock(ClassLoader.class));
        //THEN
        List<String> locations = changeLogAccessor.describeLocations();
        Assert.assertTrue(locations.size() == 1);
        Assert.assertEquals(mojo.changeLogDirectory, locations.get(0));
    }

    @Test
    public void validateGetResourceAccessorDoesNotGenerateFileAccessorsWhenChangeLogDirectoryAndSearchPathAreSet() throws IOException {

        try {
            //GIVEN
            AbstractLiquibaseChangeLogMojo mojo = new LiquibaseChangeLogMojoTest();
            mojo.project = mock(MavenProject.class);
            when(mojo.project.getBasedir()).thenReturn(new File(System.getProperty("user.dir")));
            mojo.changeLogDirectory = System.getProperty("user.dir");
            mojo.searchPath = System.getProperty("user.dir");
            //WHEN
            mojo.getResourceAccessor(mock(ClassLoader.class));
            //THEN
            Assert.fail("exception thrown");
        } catch (MojoFailureException  e) {
            Assert.assertEquals("Cannot specify searchPath and changeLogDirectory at the same time", e.getMessage());
        }
    }

    private static class LiquibaseChangeLogMojoTest extends AbstractLiquibaseChangeLogMojo {
        // For testing purposes have chose the strategy of creating an empty class as we are trying to test an abstract class
        // which otherwise we cannot instantiate
    }


}

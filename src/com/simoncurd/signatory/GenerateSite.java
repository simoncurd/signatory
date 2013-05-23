/**
 * Copyright 2013 Simon Curd <simoncurd@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.simoncurd.signatory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.simoncurd.signatory.velocity.VelocityInitialiser;

/**
 * Main class which reads a set of velocity templates from the source directory
 * and writes the parsed files to the target directory
 */
public class GenerateSite
{

	public static void main(String args[])
	{
		if (args.length != 2)
		{
			args = (new String[] { "files/source", "files/target" });
		}
		new GenerateSite(args[0], args[1]);
	}

	public GenerateSite(String source, String target)
	{
		// setup Velocity
		VelocityInitialiser.initialise(source);
		VelocityContext vctx = new VelocityContext();

		// list of files to ignore
		Properties ignores = new Properties();
		ignores.put("ignore.properties", "");

		// load ignores from file
		File ignoreFile = new File((new StringBuilder()).append(source).append("/ignore.properties").toString());
		if (ignoreFile.exists())
		{
			try
			{
				ignores.load(new FileReader(ignoreFile));
			}
			catch (IOException e)
			{
				throw new RuntimeException("error loading ignore.properties", e);
			}
		}

		File sourceDirectory = new File(source);

		for (File file : sourceDirectory.listFiles())
		{
			String filename = file.getName();
			if (ignores.containsKey(filename) || file.isDirectory())
			{
				continue;
			}
			String outputFilename = (new StringBuilder()).append(filename.substring(0, filename.lastIndexOf('.'))).append(".html").toString();
			System.out.println("generating [" + outputFilename + "]");
			try
			{
				FileWriter out = new FileWriter(new File((new StringBuilder()).append(target).append("/").append(outputFilename).toString()));
				Template template = Velocity.getTemplate(filename);
				template.merge(vctx, out);
				out.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}

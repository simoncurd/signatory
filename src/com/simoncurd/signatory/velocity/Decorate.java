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
package com.simoncurd.signatory.velocity;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Parse;
import org.apache.velocity.runtime.parser.node.Node;

/**
 * Custom Velocity directive to specify that a block of content
 * should be decorated. <br/>
 * <br/>
 * 
 * Example usage: The velocity page specifies a separate template for the layout<br/>
 * 
 * <pre>
 * {@code
 * #decorate("layout.vm")
 *    <h1>Page Title</h1>
 * #end
 * }
 * </pre>
 * 
 * The layout template replaces <code>$body_content</code> with the decorated content: <br/>
 * 
 * <pre>
 * {@code
 * <html>
 *    <body>
 *       $body_content
 *    </body>
 * </html>
 * }
 * </pre>
 */
public class Decorate extends Parse
{

	public String getName()
	{
		return "decorate";
	}

	public int getType()
	{
		return BLOCK;
	}

	public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException
	{
		StringWriter sw = new StringWriter();
		node.jjtGetChild(1).render(context, sw);

		// store the contents against the variable
		context.put("body_content", sw.toString());

		return super.render(context, writer, node);
	}
}

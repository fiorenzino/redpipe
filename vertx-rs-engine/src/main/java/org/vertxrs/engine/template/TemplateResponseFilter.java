package org.vertxrs.engine.template;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.core.interception.jaxrs.SuspendableContainerResponseContext;

@Provider
public class TemplateResponseFilter implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		if(responseContext.getEntityClass() == Template.class) {
			SuspendableContainerResponseContext ctx = (SuspendableContainerResponseContext) responseContext;
			ctx.suspend();
			Template template = (Template) responseContext.getEntity();
			template.render().subscribe(resp -> {
				ctx.setEntity(resp.getEntity());
				ctx.setStatus(resp.getStatus());
				ctx.resume();
			}, err -> ctx.resume(err));
		}
	}

}

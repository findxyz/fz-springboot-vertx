package xyz.fz.springBootVertx.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;

public class BaseFilter implements ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(BaseFilter.class);

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        logger.info("filter: {}", containerRequestContext.getUriInfo().getAbsolutePath());
    }
}

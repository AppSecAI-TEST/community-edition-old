package com.activiti.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.activiti.repo.node.NodeService;
import com.activiti.repo.ref.StoreRef;
import com.activiti.web.bean.repository.Repository;

/**
 * ServletContextListener implementation that initialises the application.
 * 
 * NOTE: This class must appear after the Spring context loader listener
 * 
 * @author gavinc
 */
public class ContextListener implements ServletContextListener, HttpSessionListener
{
   private static Logger logger = Logger.getLogger(ContextListener.class);
   
   /**
    * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
    */
   public void contextInitialized(ServletContextEvent event)
   {
      // make sure that the spaces store in the repository exists
      WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());
      NodeService nodeService = (NodeService)ctx.getBean("indexingNodeService");
         
      if (nodeService.exists(Repository.getStoreRef()) == false)
      {
         // create the store
         nodeService.createStore(StoreRef.PROTOCOL_WORKSPACE, Repository.REPOSITORY_STORE);
      }
   }

   /**
    * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
    */
   public void contextDestroyed(ServletContextEvent event)
   {
      // nothing to do
   }

   public void sessionCreated(HttpSessionEvent event)
   {
      logger.info("HTTP session created: " + event.getSession().getId());
   }

   public void sessionDestroyed(HttpSessionEvent event)
   {
      logger.info("HTTP session destroyed: " + event.getSession().getId());
   }
}

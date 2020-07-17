package controller;

import model.DBmanager;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class plabContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext sc = event.getServletContext();
        DBmanager db = new DBmanager();
        sc.setAttribute("db", db);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}

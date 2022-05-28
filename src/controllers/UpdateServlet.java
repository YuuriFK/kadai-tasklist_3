package controllers;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Task;
import utils.DBUtil;

@WebServlet(asyncSupported = true, urlPatterns = { "/update" })
public class UpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UpdateServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token =  request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            Task t = em.find(Task.class, (Integer)(request.getSession().getAttribute("task_id")));

            String content = request.getParameter("content");
            t.setContent(content);

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            t.setUpdated_at(currentTime);

            // データベースを更新
            //データベースから取得したデータに変更をかけてコミットすれば変更が反映されるので em.persist(m); は不要
            em.getTransaction().begin();
            em.getTransaction().commit();
            em.close();

            //セッションスコープの不要なデータを削除
            request.getSession().removeAttribute("task.id");

            response.sendRedirect(request.getContextPath() + "/index");
        }
    }

}

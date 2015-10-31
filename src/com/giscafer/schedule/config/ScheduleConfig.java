package com.giscafer.schedule.config;

import com.demo.blog.Blog;
import com.demo.blog.BlogController;
import com.demo.index.BlogIndexController;
import com.demo.index.IndexController;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.UrlSkipHandler;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;

/**
 * API引导式配置
 */
public class ScheduleConfig extends JFinalConfig {
	
	/**
	 * 配置常量
	 * 1：模板路径= BaseViewPath + ViewPath + render时的参数
	 * 
	 * 2：当 render 时view的参数以 "/" 打头，则模板路径使用从WebRoot下的绝对路
	 * 
	 * 3：在配置路由时如果省略第三个参数，则 viewPath = controllerKey
	 */
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用PropKit.get(...)获取值
		PropKit.use("a_little_config.txt");
		//设置基础路径
		me.setDevMode(PropKit.getBoolean("devMode", false));
		me.setBaseViewPath("/WEB-INF/views/");
		me.setError401View("/WEB-INF/views/error/401.html");
		me.setError403View("/WEB-INF/views/error/403.html");
		me.setError404View("/WEB-INF/views/error/404.html");
		me.setError500View("/WEB-INF/views/error/500.html");
	}
	
	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		me.add("/", IndexController.class);
		me.add("/index", IndexController.class,"/index");	// 第三个参数为该Controller的视图存放路径
		me.add("/hello", BlogIndexController.class,"/error");
		me.add("/blog", BlogController.class,"/blog");			// 第三个参数省略时默认与第一个参数值相同，在此即为 "/blog"
	}
	
	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		// 配置C3p0数据库连接池插件
		C3p0Plugin c3p0Plugin = new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
		me.add(c3p0Plugin);
		
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		me.add(arp);
		arp.addMapping("blog", Blog.class);	// 映射blog 表到 Blog模型
	}
	
	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
		
	}
	
	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
		//使用此方式跳过jfinal过滤器对servlet的拦截
		me.add(new UrlSkipHandler("/home", false));
	}
	
	/**
	 * 建议使用 JFinal 手册推荐的方式启动项目
	 * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
	 */
	public static void main(String[] args) {
		JFinal.start("WebRoot", 8080, "/FinalScheduler", 5);
	}
}
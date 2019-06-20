# Shiro_Demo
1、项目结构
    一个Maven项目，俩个module组成。
2、项目说明
    Shiro_Authentication模块内容主要为Shiro的认证、授权、加密以及Shiro自带Realm和自定义Realm使用等Demo。
    Shiro_Springboot模块内容有以下几点：
      1、集成springboot；
      2、配置自定义Filter；
      3、为接口配置注解管理接口访问权限；
      4、集成Redis，配置SessionManager实现session共享；
      5、集成Redis，配置CacheManager实现Shiro缓存功能，避免过多访问Redis，提高服务性能；
      6、实现Shiro RememberMe功能；

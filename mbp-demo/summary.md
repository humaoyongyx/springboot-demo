# 说明



```
#mybatis:
#  type-aliases-package: issac.study.mybatis.domain
#  mapper-locations: classpath:mybatis/mapper/*.xml
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  type-aliases-package: issac.study.mybatis.domain
  mapper-locations: classpath:mybatis/mapper/*.xml
#  config-location: classpath:mybatis-config.xml
```

mybatis有三大配置，model、mapper和dao，以及一个全局mybatis配置 

model指的就是实体bean

mapper指的就是xml的sql配置

dao指的就是mapper对应的代码接口形式
```
@MapperScan("issac.study.mybatis.mapper")

```

所谓的全局配置，可以在其里面配置plugin以及日志等，如果配置了，不能在右configuration配置

手动的过程如下：

mybatis工作原理，详细见：

https://blog.csdn.net/luanlouis/article/details/40422941

```
package com.louis.mybatis.test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.louis.mybatis.model.Employee;

/**
 * SqlSession 简单查询演示类
 * @author louluan
 */
public class SelectDemo {

	public static void main(String[] args) throws Exception {
		/*
		 * 1.加载mybatis的配置文件，初始化mybatis，创建出SqlSessionFactory，是创建SqlSession的工厂
		 * 这里只是为了演示的需要，SqlSessionFactory临时创建出来，在实际的使用中，SqlSessionFactory只需要创建一次，当作单例来使用
		 */
		InputStream inputStream = Resources.getResourceAsStream("mybatisConfig.xml");
		SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
		SqlSessionFactory factory = builder.build(inputStream);
		
		//2. 从SqlSession工厂 SqlSessionFactory中创建一个SqlSession，进行数据库操作
		SqlSession sqlSession = factory.openSession();
	
		//3.使用SqlSession查询
		Map<String,Object> params = new HashMap<String,Object>();
		
		params.put("min_salary",10000);
		//a.查询工资低于10000的员工
		List<Employee> result = sqlSession.selectList("com.louis.mybatis.dao.EmployeesMapper.selectByMinSalary",params);
		//b.未传最低工资，查所有员工
		List<Employee> result1 = sqlSession.selectList("com.louis.mybatis.dao.EmployeesMapper.selectByMinSalary");
		System.out.println("薪资低于10000的员工数："+result.size());
		//~output :   查询到的数据总数：5  
		System.out.println("所有员工数: "+result1.size());
		//~output :  所有员工数: 8
	}

}

```




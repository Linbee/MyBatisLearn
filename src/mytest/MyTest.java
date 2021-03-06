package mytest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import dao.DepartmentDao;
import dao.EmployeeDao;
import pojo.Department;
import pojo.Employee;
import pojo.EmployeeMapperAnnotation;
import pojo.EmployeePlus;

public class MyTest {

	private EmployeePlus employPlusById;

	/*
	 * 根据xml配置文件，创建SqlSessionFactory对象
	 * 		有数据源的一些运行环境信息
	 * sql映射文件，配置了每一个sql，以及sql的封装规则等
	 * 将sql映射文件注册证全局配置文件中
	 * 代码编写
	 * 		跟去全局配置文件得到SqlSessionFactory
	 * 		使用sqlSession工厂，获取到sqlSession对象使用来完成增删改查
	 * 		一个sqlSession就是代表着和数据库的一次会话，用完需要关闭
	*/
	@Test
	public void start() throws IOException
	{
		String resource ="mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSession=new SqlSessionFactoryBuilder().build(inputStream);
	
		SqlSession openSession = sqlSession.openSession();
	
		Employee emp=openSession.selectOne("selectEmp",1);
		
		openSession.commit();
		
		openSession.close();
		
		System.out.println(emp);
		
	}
	
	public SqlSession getSqlSession()
	{
		SqlSession sqlSession=null;
		try
		{
			String resource ="mybatis-config.xml";
			InputStream inputStream=Resources.getResourceAsStream(resource);
			SqlSessionFactory sqlSessionFactory =new SqlSessionFactoryBuilder().build(inputStream);
			sqlSession=sqlSessionFactory.openSession(true);
		}catch (Exception e) {
			// TODO: handle exception
		}
		return sqlSession;
	}
	
	
	@Test
	public void interfaceTest()
	{
		SqlSession sqlSession=null;
		try
		{	
			/*创建sqlSessionFactory 获取sqlSession*/
			String resource="mybatis-config.xml";
			InputStream inputStream=Resources.getResourceAsStream(resource);
			SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
			sqlSession=sqlSessionFactory.openSession();
		
			
			/*mybaits的接口编程
			 * 
			*/
			EmployeeDao employeeDao=sqlSession.getMapper(EmployeeDao.class);
			Employee emp=employeeDao.getEmployeeById(7);
			sqlSession.commit();
			System.out.println(emp);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		finally
		{
			sqlSession.close();
		}
	}
	
	@Test
	public void testAnnotation()
	{
		SqlSession sqlSession=getSqlSession();
		EmployeeMapperAnnotation employeeMapperAnnotation= sqlSession.getMapper(EmployeeMapperAnnotation.class);
		System.out.println(employeeMapperAnnotation.getEmpById(1));
	}
	
	@Test
	public void testCRUD()
	{
		SqlSession sqlSession = null;
		try
		{
			String resource="mybatis-config.xml";
			InputStream inputStream = Resources.getResourceAsStream(resource);
			SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
			sqlSession = sqlSessionFactory.openSession(true); //开启自动提交
			
			EmployeeDao employeeDao=sqlSession.getMapper(EmployeeDao.class);
			Employee emp=employeeDao.getEmployeeById(1);
			
			//employeeDao.addEmployee(new Employee("sccc","sccc@mail.com","0"));
			
			Employee old = employeeDao.getEmployeeById(1);
			old.setEmail("oldtom33@mail.com");
			old.setLastName("old_Tom");
			employeeDao.updateEmployee(old);
			
			employeeDao.deleteEmployeeById(3);
			
		
		}catch (Exception e) {
			// TODO: handle exception
		}
		finally {
			sqlSession.close();
		}
	}
	
	@Test
	public void testGeneratedKey()
	{
		try
		{
			String resource ="mybatis-config.xml";
			InputStream inputStream=Resources.getResourceAsStream(resource);
			SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
			SqlSession sqlSession =sqlSessionFactory.openSession(true);
			
			EmployeeDao employeeDao=sqlSession.getMapper(EmployeeDao.class);
			
			Employee newEmployee = new Employee("Jery","jery@mail.com","0");
			employeeDao.addEmployee(newEmployee);
			System.out.println(newEmployee.getId());
			
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			
		}
	}
	
	
	@Test
	public void testParameters()
	{
		SqlSession sqlSession = getSqlSession();
		EmployeeDao employeeDao = sqlSession.getMapper(EmployeeDao.class);
		Employee temp= employeeDao.getEmployeeById(2);
		System.out.println(temp);
		
		temp=employeeDao.getEmployeeByIdAndLastName(2, "sccc");
		
		temp.setId(5);
		temp.setLastName("Jery");
		
		temp=employeeDao.getEmployeeByPojo(temp);
		System.out.println(temp);
		
		sqlSession.commit();
	}
	
	@Test
	public void testSelectByMap()
	{
		try
		{
			String resource="mybatis-config.xml";
			InputStream inputStream = Resources.getResourceAsStream(resource);
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			SqlSession sqlSession = sqlSessionFactory.openSession(true);
			
			EmployeeDao employeeDao = sqlSession.getMapper(EmployeeDao.class);
			Map<String,Object> info = new HashMap<>();
			info.put("tableName","tbl_employee");
			info.put("id",7);
			Employee temp = employeeDao.getEmployeeByIdAndTableName(info);
			System.out.println(temp);
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		finally {
			
		}
	}
	
	@Test
	public void testSelectOtherResultType()
	{
		String resource="mybatis-config.xml";
		
		SqlSession sqlSession = null;
		try
		{
			InputStream inputStream = Resources.getResourceAsStream(resource);
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			sqlSession= sqlSessionFactory.openSession(true);
			
			EmployeeDao employeeDao = sqlSession.getMapper(EmployeeDao.class);
			List<Employee> list =employeeDao.getEmployeeByLastName("sccc");
			System.out.println(list);
			
			Map<String,Object> testMap = employeeDao.getEmployeeByIdReturnMap(1);
			System.out.println(testMap);
			
			Map<Integer,Employee> employeeList = employeeDao.getEmployeeByLastNameLikeReturnMap("%c%");
			System.out.println(employeeList);
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		finally {
			sqlSession.close();
		}
	}
	

//	使用resultMap自定义返回的结果集 确保在列名和类的属性名不同时正确的映射
	@Test
	public void testSelectResultMap()
	{
		String resource="mybatis-config.xml";
		
		SqlSession sqlSession = null;
		try
		{
			InputStream inputStream = Resources.getResourceAsStream(resource);
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			sqlSession= sqlSessionFactory.openSession(true);
			
			EmployeeDao employeeDao = sqlSession.getMapper(EmployeeDao.class);
			Employee temp = employeeDao.getEmployeeByIdUseResultMap(1);
			System.out.println(temp);
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		finally {
			sqlSession.close();
		}
	}
	
//	关联属性 级联查询
	@Test
	public void testSelectResultMapAboutEmployPlus()
	{
		String resource="mybatis-config.xml";
		
		SqlSession sqlSession = null;
		try
		{
			InputStream inputStream = Resources.getResourceAsStream(resource);
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			sqlSession= sqlSessionFactory.openSession(true);
			
			EmployeeDao employeeDao = sqlSession.getMapper(EmployeeDao.class);
			EmployeePlus temp = employeeDao.getEmployPlusById(7);
			System.out.println(temp);
			
		}catch (Exception e) {
			System.out.println(e);
		}
		finally {
			sqlSession.close();
		}
	}
	
//分步测试 延迟加载
	@Test
	public void testAboutEmployPlusByStep()
	{
		String resource="mybatis-config.xml";
		
		SqlSession sqlSession = null;
		try
		{
			InputStream inputStream = Resources.getResourceAsStream(resource);
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			sqlSession= sqlSessionFactory.openSession(true);
			
			EmployeeDao employeeDao = sqlSession.getMapper(EmployeeDao.class);
			EmployeePlus temp = employeeDao.getEmployeePlusByStep(7);
			System.out.println(temp.getDepartment());
			
		}catch (Exception e) {
			System.out.println(e);
		}
		finally {
			sqlSession.close();
		}
	}
	
// 鉴别器测试
	@Test 
	public void testDiscriminator()
	{
		SqlSession sqlSession = getSqlSession();
		
		EmployeeDao employeeDao = sqlSession.getMapper(EmployeeDao.class);
		EmployeePlus temp= employeeDao.getEmployeePlusByStepAndDiscriminator(8);
		System.out.println(temp);
	}

// 部门查询测试
	@Test
	public void getDepartmentById()
	{

		String resource="mybatis-config.xml";
		
		SqlSession sqlSession = null;
		try
		{
			InputStream inputStream = Resources.getResourceAsStream(resource);
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			sqlSession= sqlSessionFactory.openSession(true);
			
			DepartmentDao departmentDao= sqlSession.getMapper(DepartmentDao.class);
			Department department = departmentDao.getDepartmentById(1);
			
			System.out.println(department.getDepartmentName());
			
			department = departmentDao.getDepartmentPlusById(1);
			System.out.println(department);
			
			department = departmentDao.getDepartmentPlusByIdAndStep(1); //分步查询
			System.out.println("分步查询 "+department.getDepartmentName());
			System.out.println("分步查询 "+department.getEmps());
			
			EmployeeDao employeeDao = sqlSession.getMapper(EmployeeDao.class);
			List<EmployeePlus> list= employeeDao.getAllEmployeeBydepartmentId(1);
			System.out.println(list);
			
			
			
		}catch (Exception e) {
			System.out.println(e);
		}
		finally {
			sqlSession.close();
		}
	}

//动态SQL测试
	@Test
	public void testDynamicSQL()
	{
		SqlSession session =getSqlSession();
		try
		{
			
			EmployeeDao employeeDao = session.getMapper(EmployeeDao.class);
			Employee test = new Employee();
			test.setLastName("t");
			
			List<Employee> list = employeeDao.getEmployeeByConditionIf(test); //if test
			System.out.println(list);
			
			
			/*list = employeeDao.getEmployeeByConditionChoose(new Employee()); // choose test
			System.out.println(list);
			
			test.setId(7);
			test.setLastName("sbbb");
			test.setEmail("sbbb@mail.com");
			employeeDao.updateEmployeeByDynamic(test);*/
			
			List<EmployeePlus> testInsert = new ArrayList<EmployeePlus>(); //批量添加
			/*testInsert.add(new EmployeePlus("Tiny", "tiny@mail.com", "0", new Department(1)));
			testInsert.add(new EmployeePlus("Funy", "Funy@mail.com", "1", new Department(2)));
			employeeDao.addEmployeeDynamic(testInsert);*/
			
/*			testInsert.clear();
			EmployeePlus temp=null;
			//temp.setLastName("Tiny");
			testInsert=employeeDao.getEmployeeTestInnerParameter(temp);
			System.out.println(testInsert);*/
		}
		catch (Exception e) {
			System.out.println(e);
		}
		finally
		{
			session.close();
		}
	}
	
	/**
	 * 
	 */
	/**
	 * 
	 */
	/**
	 * 
	 */
	@Test
	public void testCache()
	{
		//firstCache
		SqlSession session = getSqlSession();
		try
		{
			EmployeeDao employeeDao=session.getMapper(EmployeeDao.class);
			EmployeePlus temp= employeeDao.getEmployPlusById(7);
			System.out.println(temp);
			
			
			EmployeePlus temp02= employeeDao.getEmployPlusById(7);
			System.out.println(temp02);
		}
		catch (Exception e) {
			System.out.println(e);
		}
		finally {
			session.close();
		}
		
		//second cache
		try {
			SqlSession session01 = getSqlSession();
			SqlSession session02 = getSqlSession();
			
			EmployeeDao employeeDao01=session01.getMapper(EmployeeDao.class);
			EmployeePlus temp = employeeDao01.getEmployPlusById(8);
			System.out.println(temp);
			session01.close();
			
			
			EmployeeDao employeeDao02 = session02.getMapper(EmployeeDao.class);
			session02.close();
		}catch (Exception e) {
			// TODO: handle exception
		} 
		finally {
			// TODO: handle finally clause
		}
	}
}

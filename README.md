# wfaas-adapter-orchestrate

## Getting Started

The easiest way to get start with a Spring application is to define a collection entity using standard Spring Data annotations.

### Entity Class

	@Collection("MyCollectionName")
	public class MyObject {
	    
	    @Id
	    private String id = UUID.randomUUID().toString();
	    private String stringProperty;
	
	    public String getId() {
	        return id;
	    }
	
	    public void setId(String id) {
	        this.id = id;
	    }
	    
	    public String getStringProperty() {
	        return stringProperty;
	    }
	
	    public void setStringProperty(String stringProperty) {
	        this.stringProperty = stringProperty;
	    }
	
	}

You will notice that the `@Collection` annotation has been applied in this case. When the collection name differs from the entity type's simple name, this annoatation can be used to specify the collection name.

### Repository Interface

After you have created and entity class, simply extend our OrchestrateRepository interface. This provides standard CRUD operations, no additional code is required.

	public interface MyRespository extends OrchestrateRepository<MyObject, String>{

	}

### Additional Configuration

The best way to configure you application to detect a repository is our `@EnableOrchestrateRepositories` annotation. This annotation allows you to define the location of you repository interface classes. The configuration will also make these interfaces available to the application as injectable beans in the application context.

    @Configuration
    @EnableOrchestrateRepositories("path.to.my.repository.interface")
    public class Application {
        
        @Bean
        public OrchestrateTemplate orchestrateTemplate() {
            
            OrchestrateTemplate template = new OrchestrateTemplate();
            template.setApiKey("API-KEY-GOES-HERE");
            
            return template;
        
        }
        
    } 

The two configuration elements to pay attention to here are the `@EnableOrchestrateRepositories` annotation defining the location of our interface, as well as the bean definition method creating the OrchestrationTemplate instance. The template is the source of all Orchestrate client operations. This template has several configuration options related to the Orchestrate API and endpoint configuration.

### Repository Injection

Now, simply inject your repository into any bean you would like using the standard `@Autowire` annotation.

    @Autowired
    private MyRespository repository;
    
## Dynamic Query Interfaces

Spring Data allows for the dynamic parsing and construction of query methods based on interface methods. What follows are functions that are currently supported by the adapter.

### Find By With Query Parameters

You can define a dynamic method to locate a single entity.

	public interface MyRespository extends OrchestrateRepository<MyObject, String>{
	
		MyObject findByFieldName(String value);

	}
	
You can define a dynamic method to locate a list of entities.

	public interface MyRespository extends OrchestrateRepository<MyObject, String>{
	
		List<MyObject> findByFieldName(String value);

	}
	
You can define a dynamic method to locate to page through entities.

	public interface MyRespository extends OrchestrateRepository<MyObject, String>{
	
		Page<MyObject> findByFieldName(String value, Pageable req);

	}
	
You can define a dynamic method with a slightly more complex parameter combination.

	public interface MyRespository extends OrchestrateRepository<MyObject, String>{
	
		List<MyObject> findByField1NameAndField2Name(String value1, String value2);

	}
	
You can define a dynamic method with complex types using nested parameter syntax.

	public interface MyRespository extends OrchestrateRepository<MyObject, String>{
	
		List<MyObject> findByObjectField_NestedField(String value);

	}
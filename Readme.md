# Overview
This project is an AI-powered chatbot built with Spring Boot, Spring AI, and Azure OpenAI that uses RAG (Retrieval Augmented Generation) and vector store with Spring AI.
Thanks to that, the Spring Boot app will retrieve similar documents that best match a user query before sending a request to the AI model. These documents provide context for the query and are sent to the AI model alongside the user’s question.
It integrates Azure Cognitive Services and Kubernetes to provide intelligent, scalable, and secure chatbot solutions for businesses.

<img title="Retrieval Augmented Generation (RAG) technique" alt="Alt text" src="/images/rag.png">

## Features
- AI-Powered Chat - Uses Azure OpenAI for intelligent responses.
- Context-Aware Memory - Stores conversation history in Azure Cosmos DB.
- Multi-Channel Support - Can integrate with Web, WhatsApp, or Microsoft Teams.
- Enterprise-Grade Security - Uses OAuth2 and API Gateway for authentication.
- Scalable & Cloud-Native - Deployable on Azure App Service or Kubernetes.
- CI/CD Pipeline - Fully automated deployment with Azure DevOps and Terraform.
- Centralized Logging (Azure Monitor, Application Insights)
- Monitoring & Alerts (Azure Monitor, Prometheus/Grafana)
- Security Best Practices (Key Vault for secrets, RBAC, Network Policies)

## Architecture
### Tech Stack
- Spring Boot (Microservices)
- Spring AI (Azure OpenAI API integration)
- Azure OpenAI (GPT-based chat responses)
- Azure Cosmos DB (Chat history storage)
- Azure Functions (Webhook triggers for AI)
- Azure App Service (Scalable hosting)
- Terraform (Infrastructure as Code)
- Azure DevOps Pipelines (CI/CD automation)
- Helm & Kubernetes (Optional deployment)
## GitHub repository structure, including:
- Java components (Spring Boot chatbot API)
- Terraform scripts (to create an AKS cluster)
- Gradle build file (for dependencies and build tasks)
- Azure DevOps pipeline (CI/CD for build, package, and deploy)
- Kubernetes manifests (Deployment and LoadBalancer Service)

```
spring-ai-azure-chatbot/
│── .github/                     # GitHub workflows for CI/CD (optional)
│── infrastructure/               # Terraform configurations for AKS
│   ├── main.tf                   # Azure AKS cluster definition
│   ├── variables.tf               # Configurable variables
│   ├── outputs.tf                 # Outputs for deployments
│── src/
│   ├── main/java/com/agilesolutions/chatbot/
│   │   ├── config/                # Spring Boot configuration (AI, security, etc.)
│   │   ├── controller/            # REST API controllers (chat endpoint)
│   │   ├── service/               # Business logic for AI processing
│   │   ├── repository/            # CosmosDB repository
│   │   ├── model/                 # Domain models (User, ChatRequest, etc.)
│   ├── resources/
│   │   ├── application.yml        # Spring Boot configurations
│   │   ├── prompts/               # AI prompts for chatbot
│── test/java/com/agilesolutions/chatbot/
│   ├── service/                   # Unit tests for services
│   ├── controller/                # API integration tests
│── helm/                          # Helm charts for Kubernetes deployment
│   ├── Chart.yaml                 # Helm metadata
│   ├── values.yaml                # Configurable Helm values
│── kubernetes/                    # Kubernetes manifests
│   ├── deployment.yaml            # Deployment configuration
│   ├── service.yaml               # LoadBalancer Service
│── build.gradle                   # Gradle build file for dependencies and packaging
│── azure-pipelines.yml            # Azure DevOps pipeline for CI/CD
│── Dockerfile                      # Docker configuration
│── docker-compose.yml              # Local environment setup
│── README.md                       # Project overview & setup instructions
│── LICENSE                         # Open-source license
```

# Enhancements: Logging, Monitoring, and Security
To make the Spring AI + Azure OpenAI chatbot production-ready, let's add:
- Centralized Logging (Azure Monitor, Application Insights)
- Monitoring & Alerts (Azure Monitor, Prometheus/Grafana)
- Security Best Practices (Key Vault for secrets, RBAC, Network Policies)

## Expose Prometheus Metrics in Spring Boot
- Add Actuator + Micrometer dependencies

## Accessing metrics
```
kubectl port-forward svc/prometheus-kube-prometheus-prometheus 9090:9090
open http://localhost:9090
```

## Secure IA ChatBot
- Store API keys, DB credentials securely.
- Enable Key Vault in Spring Boot.
```
az keyvault create --name "springAIKeyVault" --resource-group "spring-ai-chatbot-rg"
az keyvault secret set --vault-name "springAIKeyVault" --name "OpenAIKey" --value "<your-key>"
```

## Implement Role-Based Access Control (RBAC)
Restrict chatbot API access using Azure Managed Identity.

Assign RBAC roles:
```
az role assignment create --assignee <service-principal-id> --role "Reader" --scope /subscriptions/<subscription-id>

Update ChatController.java to restrict access:
@PreAuthorize("hasAuthority('SCOPE_chatbot.read')")
@PostMapping
public String chat(@RequestBody String userInput) {
    // Chat logic here
}
```

##  Deploy CI/CD Updates
Modify azure-pipelines.yml to include security scans & Prometheus monitoring:
- AI Chatbot with Logging, Monitoring & Security
- Azure DevOps CI/CD with Terraform & Helm
- Observability with Prometheus, Grafana, and Azure Monitor
```
steps:
  - script: echo "Running security scan..."
  - script: ./gradlew check
  - script: |
      az aks get-credentials --resource-group spring-ai-chatbot-rg --name spring-ai-aks
      helm upgrade --install spring-ai-chatbot helm/
  - script: echo "Deploying monitoring..."
  - script: helm upgrade --install prometheus prometheus-community/kube-prometheus-stack
```


# How This Works:
- User sends a chat request to the Spring Boot Chatbot API.
- API forwards the request to Azure OpenAI (GPT) for intelligent response generation.
- The response is stored in Azure Cosmos DB for context-awareness.
- Azure Functions handle event-driven automation (e.g., analytics, logging).
- The chatbot API is deployed on Azure App Service or Kubernetes for scalability.
- CI/CD Pipeline automates builds, tests, and deployments using GitHub Actions, Azure DevOps, and Terraform.

## References
Spring Ai provides a VectorStore interface, which provides all the required functions to communicate with Vector Databases. When a user query is sent to the AI Model, it retrieves a set of Similar Documents from Vector Databases, these Documents serve as a context for user questions. this technique is also called Retrieval Augmented Generation or RAG.
- [Vector Databases perform similarity searches and return relevant data](https://wesome.org/spring-ai-vector-database)
- [Retrieval-Augmented Generation With MongoDB and Spring AI](https://www.mongodb.com/developer/languages/java/retrieval-augmented-generation-spring-ai/)
- [Provide Additional Context](https://www.baeldung.com/spring-ai-chatclient)
- [Spring Ai Simple Vector Store](https://wesome.org/spring-ai-simple-vector-store)
- [Getting Started with Spring AI VectorStore](https://howtodoinjava.com/spring-ai/vector-store-example/)
- [Using RAG and Vector Store with Spring AI](https://piotrminkowski.com/2025/02/24/using-rag-and-vector-store-with-spring-ai/)
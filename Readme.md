# Overview
This project is an AI-powered chatbot built with Spring Boot, Spring AI, and Azure OpenAI that uses RAG (Retrieval Augmented Generation) and vector store with Spring AI.
Thanks to that, the Spring Boot app will retrieve similar documents that best match a user query before sending a request to the AI model. These documents provide context for the query and are sent to the AI model alongside the user’s question.
Vector databases, through semantic searches, help with addressing some of the issues with LLMs such as hallucinations.
It integrates Azure Cognitive Services and Kubernetes to provide intelligent, scalable, and secure chatbot solutions for businesses.

## Why RAG
Large language models (LLMs) like ChatGPT are trained on public internet data that was available at the point in time when they were trained. They can answer questions related to the data they were trained on.
Retrieval Augmented Generation (RAG) is a smart way to improve how AI systems answer questions or create content by combining two steps: retrieving useful information and generating responses. 
Instead of just relying on what the AI knows, RAG pulls in extra data that helps the system understand the question better and provide more accurate, context-aware answers.
What you will see on this DEMO is that I will pull stocks, forex and other financial assets from [twelvedata](https://twelvedata.com/) over REST API and store that data as 
vectors (text data converted to number sequences using an embedding model) on [Azure AI Search](https://learn.microsoft.com/en-us/azure/search/search-what-is-azure-search) ([formerly known as "Azure Cognitive Search"](https://learn.microsoft.com/en-us/azure/search/whats-new#new-service-name)) as the primary index store.


- ### Benefits and Applications of RAG
- Improved privacy: You can use data that the AI wasn’t trained on, meaning you don’t have to worry about the AI knowing sensitive information beforehand.
- Better context: The system can pull in relevant information to understand the user’s question more deeply. 
  - This demo pulls in actual realtime stocks from [TwelveData](https://support.twelvedata.com/), See [TwelveData API contracts](https://twelvedata.com/docs#core-data)
  - Converted to vectors accordingly stored in a vector Database (VectorStore)
.  - Uses an advisor to push actual context to the prompt to improving the results calculated for GPT static learned data...
- Higher accuracy: By looking up information, RAG helps reduce mistakes (when AI makes up things) by using real facts.
- Flexible applications: It can be used for various tasks like answering questions, creating summaries, or powering chatbots.

<img title="Retrieval Augmented Generation (RAG) technique" alt="Alt text" src="/images/rag.png">

I will set up a Vector Store to implement a RAG example. The Vector Store holds our data along with its vector embeddings, allowing us to perform semantic searches and find the most relevant information for a user’s query.

## What is on this project
- Terraform IaC configs to setup an AKS cluster and [Azure AI](https://learn.microsoft.com/en-us/azure/ai-foundry/what-is-ai-foundry) Foundry HUB and project and finally deploying gpt-4 LLM model.
- [SpringBoot AI](https://docs.spring.io/spring-ai/reference/index.html) project
  - Connecting to Azure AI project
  - Connects to AI VectorStore to loading growth stock trends from [TwelveData](https://twelvedata.com/) to supporting AI RAG.
  - Exposing REST API endpoints to invoking AI ChatBot clients, invoking Azure OpenAI for intelligent responses
- AI-Powered Chat - Uses Azure OpenAI for intelligent responses.
- Enterprise-Grade Security - Uses OAuth2 and API Gateway for authentication.
- Scalable & Cloud-Native - Deployable on Azure App Service or Kubernetes.
- CI/CD Pipeline - Fully automated deployment with Azure DevOps, HELM and Terraform.
- Centralized Logging (Azure Monitor, Application Insights)
- Monitoring & Alerts (Azure Monitor, Prometheus/Grafana)
- Security Best Practices (Key Vault for secrets, RBAC, Network Policies)

## Terraform IaC provision Azure AKS cluster and Azure AI HUB and project
1. Create an Azure account with an active subscription. You can [create an account for free](https://azure.microsoft.com/en-us/pricing/purchase-options/azure-account?icid=azurefreeaccount&WT.mc_id=A261C142F).
2. [Install and configure Terraform](https://learn.microsoft.com/en-us/azure/developer/terraform/quickstart-configure)
3. Run the following commands...
```
cd ./terraform-manifests
terraform init
terraform validate
terraform plan -out tfplan
terraform apply "tfplan"
```
Check the outputs and assign the Azure OpenAI key, endpoint and model on application.yml.

## Architecture
### Tech Stack
- Spring Boot (Microservices)
- Spring AI (Azure OpenAI API integration)
- Azure AI foundry OpenAI (GPT-based chat responses)
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

## start ollama locally
- docker compose -f docker-compose.yml
- docker run -d -v ollama:/root/.ollama -p 11434:11434 --name ollama ollama/ollama
- docker logs -f ollama
- docker exec -ti ollama ollama run llama2
- Ask some question: >>>> who is the current PM in Netherlands
- curl -X POST http://localhost:11434/api/generate -d '{ "model": "llama2","prompt":"List down 5 best java frameworks"

## Evalutation testing
Evaluating the generated content to ensure the AI models do not generate hallucinated responses. Spring AI Evaluators to test LLM help checking the relevance and factual accuracy of the LLM response, namely RelevanceEvaluator and FactCheckingEvaluator.
- RelevancyEvaluator: JUnit test that performs a RAG query over a Twelvedata share document loaded into a Vector Store and then evaluates if the response is relevant to the user text
- FactCheckingEvaluator: Junit tests to detect and reduce hallucinations in AI outputs by verifying if a given statement (claim) is logically supported by the provided context (document).

## References
The Spring AI project aims to streamline the development of applications that incorporate artificial intelligence functionality without unnecessary complexity.
Azure AI Foundry provides a unified platform for enterprise AI operations, model builders, and application development. 
- [TwelveData: Financial Data that drives your success](https://twelvedata.com/)
- [About Spring AI](https://docs.spring.io/spring-ai/reference/index.html)
- [About Azure AI Foundry](https://learn.microsoft.com/en-us/azure/ai-foundry/what-is-ai-foundry)
- [Azure AI Foundary Retrieval augmented generation and indexes](https://learn.microsoft.com/en-us/azure/ai-foundry/concepts/retrieval-augmented-generation)
- [How to build and consume vector indexes in Azure AI Foundry portal](https://learn.microsoft.com/en-us/azure/ai-foundry/how-to/index-add)
- [What's Azure AI Search](https://learn.microsoft.com/en-us/azure/search/search-what-is-azure-search)
- [Create an Azure AI Foundry hub with Terraform](https://learn.microsoft.com/en-us/azure/ai-foundry/how-to/create-hub-terraform?tabs=azure-cli)
- [Quickstart: Create an Azure AI services resource using Terraform](https://learn.microsoft.com/en-us/azure/ai-services/create-account-terraform?tabs=azure-cli)
- [Terraform Azure AI Hub module](https://registry.terraform.io/modules/Azure/avm-res-machinelearningservices-workspace/azurerm/latest/examples/private_ai_studio)
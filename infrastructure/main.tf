 provider "azurerm" {
│     features {}
│   }
│
│   resource "azurerm_kubernetes_cluster" "aks" {
│     name                = "chatbot-aks"
│     location            = "East US"
│     resource_group_name = "chatbot-rg"
│     dns_prefix          = "chatbot"
│     default_node_pool {
│       name       = "default"
│       node_count = 2
│       vm_size    = "Standard_DS2_v2"
│     }
│     identity {
│       type = "SystemAssigned"
│     }
│   }
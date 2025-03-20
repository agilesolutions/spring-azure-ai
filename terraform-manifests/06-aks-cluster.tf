resource "azurerm_kubernetes_cluster" "aks" {
  name                = local.cluster_name
  location            = var.resoure_group_location
  resource_group_name = local.rg_name
  dns_prefix          = "ai"
  default_node_pool {
    name       = "default"
    node_count = 2
    vm_size    = "Standard_DS2_v2"
  }
# Identity (System Assigned or Service Principal)
  identity { type = "SystemAssigned" }

  # Network Profile
  network_profile {
    load_balancer_sku = "Standard"
    network_plugin = "azure"
  }

  # Linux Profile
  linux_profile {
    admin_username = "ubuntu"
    ssh_key {
        key_data = file(data.external.ssh_key_generator.result.public_key)
    }
  }

}
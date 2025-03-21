# https://registry.terraform.io/providers/hashicorp/azurerm/latest/docs/resources/ai_foundry_project
data "azurerm_client_config" "current" {}

resource "azurerm_key_vault" "ai_kv" {
  name                = "aikv"
  location            = azurerm_resource_group.aks_rg.location
  resource_group_name = azurerm_resource_group.aks_rg.name
  tenant_id           = data.azurerm_client_config.current.tenant_id

  sku_name                 = "standard"
  purge_protection_enabled = true
}

resource "azurerm_key_vault_access_policy" "ai_kv_ap" {
  key_vault_id = azurerm_key_vault.ai_kv.id
  tenant_id    = data.azurerm_client_config.current.tenant_id
  object_id    = data.azurerm_client_config.current.object_id

  key_permissions = [
    "Create",
    "Get",
    "Delete",
    "Purge",
    "GetRotationPolicy",
  ]
}

resource "azurerm_storage_account" "ai_sa" {
  name                     = "aisa"
  location                 = azurerm_resource_group.aks_rg.location
  resource_group_name      = azurerm_resource_group.aks_rg.name
  account_tier             = "Standard"
  account_replication_type = "LRS"
}

resource "azurerm_ai_services" "ai_service" {
  name                = "aiservice"
  location            = azurerm_resource_group.aks_rg.location
  resource_group_name = azurerm_resource_group.aks_rg.name
  sku_name            = "S0"
}

resource "azurerm_ai_foundry" "ai_hub" {
  name                = "aihub"
  location            = azurerm_ai_services.ai_service.location
  resource_group_name = azurerm_resource_group.aks_rg.name
  storage_account_id  = azurerm_storage_account.ai_sa.id
  key_vault_id        = azurerm_key_vault.ai_kv.id

  identity {
    type = "SystemAssigned"
  }
}

resource "azurerm_ai_foundry_project" "ai_project" {
  name               = "aiproject"
  location           = azurerm_ai_foundry.ai_hub.location
  ai_services_hub_id = azurerm_ai_foundry.ai_hub.id
}

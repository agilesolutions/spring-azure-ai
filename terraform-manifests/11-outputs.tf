# Output Values - Resource Group
output "resource_group_id" {
  description = "Resource Group ID"
  # Atrribute Reference
  value = azurerm_resource_group.aks_rg.id
}
output "resource_group_name" {
  description = "Resource Group name"
  # Argument Reference
  value = azurerm_resource_group.aks_rg.name
}
output "openai_endpoint" {
  description = "The endpoint used to connect to the Cognitive Service Account"
  # Argument Reference
  value = azurerm_ai_services.ai_service.endpoint
}
output "openai_primary_key" {
  description = "The primary access key for the Cognitive Service Account"
  # Argument Reference
  value = azurerm_ai_services.ai_service.primary_access_key
}

output "openai_model" {
  description = "The primary access key for the Cognitive Service Account"
  # Argument Reference
  value = azurerm_cognitive_deployment.ai_deployment.model[0].name
}
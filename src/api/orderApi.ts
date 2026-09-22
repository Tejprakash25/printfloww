import { apiRequest } from "./apiClient";

export interface CreateOrderRequest {
  title: string;
  description: string;
  productType: string;
  material: string;
  quantity: number;
  size?: string;
  color?: string;
  paperType?: string;
  deliveryType: string;
}

export async function getOrders() {
  return apiRequest("/orders");
}

export async function getOrder(orderId: number) {
  return apiRequest(`/orders/${orderId}`);
}

export async function createOrder(data: CreateOrderRequest) {
  return apiRequest("/orders", {
    method: "POST",
    body: JSON.stringify(data),
  });
}

export async function getOrderHistory(orderId: number) {
  return apiRequest(`/orders/${orderId}/history`);
}
import { Customer, Ticket, AlarmEvent, CreateAlarmRequest } from './types';

// All API calls use relative paths that get proxied through React dev server
// The proxy configuration in package.json handles routing to backend services

// Alarm Service API
export const alarmApi = {
    getAlarms: async (): Promise<AlarmEvent[]> => {
        const response = await fetch('/api/alarms');
        if (!response.ok) throw new Error('Failed to fetch alarms');
        return response.json();
    },

    createAlarm: async (alarm: CreateAlarmRequest): Promise<AlarmEvent> => {
        const response = await fetch('/api/alarms/start', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(alarm)
        });
        if (!response.ok) throw new Error('Failed to create alarm');
        return response.json();
    }
};

// Customer Service API  
export const customerApi = {
    getCustomer: async (id: string): Promise<Customer> => {
        const response = await fetch(`/api/customers/${id}`);
        if (!response.ok) throw new Error('Failed to fetch customer');
        return response.json();
    }
};

// Ticket Service API
export const ticketApi = {
    getTickets: async (): Promise<Ticket[]> => {
        const response = await fetch('/api/tickets');
        if (!response.ok) throw new Error('Failed to fetch tickets');
        return response.json();
    },

    updateTicketStatus: async (ticketId: string, status: string): Promise<Ticket> => {
        const response = await fetch(`/api/tickets/${ticketId}/status?status=${status}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' }
        });
        if (!response.ok) throw new Error('Failed to update ticket status');
        return response.json();
    }
};

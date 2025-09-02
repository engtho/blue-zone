export interface Customer {
    id: string;
    name: string;
    email: string;
    phone: string;
    services: string[];
    priority: number;
    region: string;
}

export interface Ticket {
    ticketId: string;
    alarmId: string;
    customerId: string;
    status: string;
    createdAt: number;
    description: string;
    priority?: number;
}

export interface AlarmEvent {
    alarmId: string;
    service: string;
    impact: string;
    affectedCustomers: string[];
    timestamp: number;
}

export interface CreateAlarmRequest {
    service: string;
    impact: string;
    affectedCustomers: string[];
}

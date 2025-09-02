import React, { useState, useEffect } from 'react';
import { Ticket, Customer } from '../types';
import { ticketApi, customerApi } from '../api';

interface TicketWithCustomer extends Ticket {
    customer?: Customer;
}

const TicketList: React.FC = () => {
    const [tickets, setTickets] = useState<TicketWithCustomer[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchTicketsWithCustomers = async () => {
            try {
                setLoading(true);
                const ticketData = await ticketApi.getTickets();

                // Fetch customer details for each ticket
                const ticketsWithCustomers = await Promise.all(
                    ticketData.map(async (ticket) => {
                        try {
                            const customer = await customerApi.getCustomer(ticket.customerId);
                            return { ...ticket, customer };
                        } catch (err) {
                            console.error(`Failed to fetch customer ${ticket.customerId}:`, err);
                            return ticket;
                        }
                    })
                );

                setTickets(ticketsWithCustomers);
            } catch (err) {
                setError(err instanceof Error ? err.message : 'Failed to fetch tickets');
            } finally {
                setLoading(false);
            }
        };

        fetchTicketsWithCustomers();

        // Refresh every 5 seconds
        const interval = setInterval(fetchTicketsWithCustomers, 5000);
        return () => clearInterval(interval);
    }, []);

    const handleResolveTicket = async (ticketId: string) => {
        try {
            await ticketApi.updateTicketStatus(ticketId, 'RESOLVED');
            // Refresh tickets immediately to show the update
            setTickets(prev =>
                prev.map(ticket =>
                    ticket.ticketId === ticketId
                        ? { ...ticket, status: 'RESOLVED' }
                        : ticket
                )
            );
        } catch (err) {
            console.error('Failed to resolve ticket:', err);
            setError(err instanceof Error ? err.message : 'Failed to resolve ticket');
        }
    };

    const formatTimestamp = (timestamp: number) => {
        return new Date(timestamp * 1000).toLocaleString();
    };

    const getPriorityBadge = (priority?: number) => {
        if (priority === 1) {
            return <span className="badge critical">Critical</span>;
        }
        return <span className="badge standard">Standard</span>;
    };

    const getStatusBadge = (status: string) => {
        const statusClass = status.toLowerCase().replace('_', '-');
        return <span className={`badge ${statusClass}`}>{status}</span>;
    };

    if (loading) return <div className="loading">Loading tickets...</div>;
    if (error) return <div className="error">Error: {error}</div>;

    return (
        <div className="ticket-list">
            <h2>Support Tickets ({tickets.length})</h2>

            {tickets.length === 0 ? (
                <div className="empty-state">
                    <p>No tickets found. Create an alarm to generate tickets.</p>
                </div>
            ) : (
                <div className="tickets-grid">
                    {tickets
                        .sort((a, b) => (a.customer?.priority || 2) - (b.customer?.priority || 2))
                        .map((ticket) => (
                            <div key={ticket.ticketId} className="ticket-card">
                                <div className="ticket-header">
                                    <h3>Ticket #{ticket.ticketId.slice(0, 8)}</h3>
                                    <div className="badges">
                                        {getPriorityBadge(ticket.customer?.priority)}
                                        {getStatusBadge(ticket.status)}
                                    </div>
                                </div>

                                <div className="ticket-details">
                                    <div className="detail-row">
                                        <span className="label">Description:</span>
                                        <span className="value">{ticket.description}</span>
                                    </div>

                                    <div className="detail-row">
                                        <span className="label">Customer:</span>
                                        <span className="value">
                                            {ticket.customer?.name || ticket.customerId}
                                        </span>
                                    </div>

                                    {ticket.customer && (
                                        <>
                                            <div className="detail-row">
                                                <span className="label">Contact:</span>
                                                <span className="value">
                                                    {ticket.customer.email} | {ticket.customer.phone}
                                                </span>
                                            </div>

                                            <div className="detail-row">
                                                <span className="label">Services:</span>
                                                <span className="value">
                                                    {ticket.customer.services.join(', ')}
                                                </span>
                                            </div>

                                            <div className="detail-row">
                                                <span className="label">Region:</span>
                                                <span className="value">{ticket.customer.region}</span>
                                            </div>
                                        </>
                                    )}

                                    <div className="detail-row">
                                        <span className="label">Alarm ID:</span>
                                        <span className="value">{ticket.alarmId.slice(0, 8)}</span>
                                    </div>

                                    <div className="detail-row">
                                        <span className="label">Created:</span>
                                        <span className="value">{formatTimestamp(ticket.createdAt)}</span>
                                    </div>
                                </div>

                                {ticket.status === 'OPEN' && (
                                    <div className="ticket-actions">
                                        <button
                                            className="resolve-button"
                                            onClick={() => handleResolveTicket(ticket.ticketId)}
                                        >
                                            ✅ Resolve Ticket
                                        </button>
                                    </div>
                                )}
                            </div>
                        ))}
                </div>
            )}
        </div>
    );
};

export default TicketList;

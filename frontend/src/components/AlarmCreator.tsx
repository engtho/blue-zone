import React, { useState } from 'react';
import { CreateAlarmRequest } from '../types';
import { alarmApi } from '../api';

const AlarmCreator: React.FC = () => {
    const [formData, setFormData] = useState<CreateAlarmRequest>({
        service: 'BROADBAND',
        impact: 'OUTAGE',
        affectedCustomers: []
    });
    const [customerIds, setCustomerIds] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [message, setMessage] = useState<string | null>(null);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            setIsSubmitting(true);
            setMessage(null);

            const customerArray = customerIds
                .split(',')
                .map(id => id.trim())
                .filter(id => id.length > 0);

            const alarmData = {
                ...formData,
                affectedCustomers: customerArray
            };

            await alarmApi.createAlarm(alarmData);
            setMessage('Alarm created successfully! Check the tickets list.');

            // Reset form
            setCustomerIds('');
            setFormData({
                service: 'BROADBAND',
                impact: 'OUTAGE',
                affectedCustomers: []
            });

        } catch (error) {
            setMessage(`Error: ${error instanceof Error ? error.message : 'Failed to create alarm'}`);
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="alarm-creator">
            <h2>Create Service Alarm</h2>

            <form onSubmit={handleSubmit} className="alarm-form">
                <div className="form-group">
                    <label htmlFor="service">Service:</label>
                    <select
                        id="service"
                        value={formData.service}
                        onChange={(e) => setFormData({ ...formData, service: e.target.value })}
                        required
                    >
                        <option value="BROADBAND">Broadband</option>
                        <option value="MOBILE">Mobile</option>
                        <option value="TV">TV</option>
                    </select>
                </div>

                <div className="form-group">
                    <label htmlFor="impact">Impact Level:</label>
                    <select
                        id="impact"
                        value={formData.impact}
                        onChange={(e) => setFormData({ ...formData, impact: e.target.value })}
                        required
                    >
                        <option value="OUTAGE">Complete Outage</option>
                        <option value="DEGRADED">Degraded Performance</option>
                        <option value="SLOW">Slow Performance</option>
                    </select>
                </div>

                <div className="form-group">
                    <label htmlFor="customers">Affected Customer IDs (comma-separated):</label>
                    <input
                        type="text"
                        id="customers"
                        value={customerIds}
                        onChange={(e) => setCustomerIds(e.target.value)}
                        placeholder="e.g., c-42, c-100, c-300"
                        required
                    />
                    <small>Available test customers: c-42, c-7, c-100, c-200, c-300</small>
                </div>

                <button type="submit" disabled={isSubmitting} className="submit-btn">
                    {isSubmitting ? 'Creating Alarm...' : 'Create Alarm'}
                </button>
            </form>

            {message && (
                <div className={`message ${message.includes('Error') ? 'error' : 'success'}`}>
                    {message}
                </div>
            )}
        </div>
    );
};

export default AlarmCreator;

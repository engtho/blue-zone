import React from 'react';
import AlarmCreator from './components/AlarmCreator';
import TicketList from './components/TicketList';
import './App.css';

function App() {
    return (
        <div className="App">
            <header className="app-header">
                <h1>Blue Zone - Telecom Incident Management</h1>
                <p>Real-time monitoring and ticket management dashboard</p>
            </header>

            <main className="app-main">
                <div className="dashboard-grid">
                    <div className="panel">
                        <AlarmCreator />
                    </div>

                    <div className="panel wide">
                        <TicketList />
                    </div>
                </div>
            </main>

            <footer className="app-footer">
                <p>Blue Zone Workshop - Kafka Microservices Demo</p>
            </footer>
        </div>
    );
}

export default App;

import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from 'chart.js'
import { Bar } from 'react-chartjs-2'
import data from '../src/data/weeklyCheckouts.json'

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend)

export const WeeklyCheckoutsChart = () => {
    return (
        <Bar
            options={{
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                    },
                    title: {
                        display: true,
                        text: 'Chart.js Bar Chart',
                    },
                },
            }}
            data={{
                labels: data.dailyCheckouts.map((checkout) => checkout.dayOfWeek),
                datasets: [
                    {
                        label: 'Daily checkouts',
                        data: data.dailyCheckouts.map((checkout) => checkout.numberOfCheckouts),
                    },
                ],
            }}
        />
    )
}

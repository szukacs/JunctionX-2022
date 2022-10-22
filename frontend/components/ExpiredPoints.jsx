import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend,
    PointElement,
    LineElement, Filler
} from 'chart.js'
import {Line} from 'react-chartjs-2'
import data from '../src/data/weeklyCheckouts.json'

ChartJS.register(CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Filler,
    Legend)
const labels = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
export const ExpiredPoints = () => {
    return (
        <Line
            options={{
                responsive: true,
                fill: true,
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
                labels,
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
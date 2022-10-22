import { useMantineTheme } from '@mantine/core';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from 'chart.js'
import { Bar } from 'react-chartjs-2'
import data from '../src/data/dailyCheckoutsAndAwardedPoints.json'
import 'chartjs-adapter-moment'

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend)
export const DailyCheckoutChart = ({min, max}) => {
    const theme = useMantineTheme()
    return (
        <Bar
            options={{
                responsive: true,
                plugins: {
                    legend: {
                        display: false
                    },
                },
                scales: {
                    x: {
                        type: "time",
                        min,
                        max,
                    },
                },
            }}
            data={{
                labels: data.map((checkout) => checkout.date),
                datasets: [
                    {
                        label: 'Daily checkouts',
                        data: data.map((checkout) => checkout.count),
                        backgroundColor: theme.colors.teal[5]
                    },
                ],
            }}
        />
    )
}


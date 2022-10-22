import { useMantineTheme } from '@mantine/core';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from 'chart.js'
import { Line } from 'react-chartjs-2'
import data from '../src/data/dailyCheckoutsAndAwardedPoints.json'
import 'chartjs-adapter-moment'
import { transparentize } from 'polished'

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend)
export const DailyCheckoutChart = ({min, max}) => {
    const theme = useMantineTheme()
    return (
        <Line
            options={{
                responsive: true,
                fill: true,
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
                        borderColor: theme.colors.teal[5],
                        backgroundColor: transparentize(0.5, theme.colors.teal[3]),
                        pointRadius: 0
                    },
                ],
            }}
        />
    )
}


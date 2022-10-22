import { useMantineTheme } from '@mantine/core';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from 'chart.js'
import { Bar } from 'react-chartjs-2'
import data from '../src/data/weeklyCheckouts.json'

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend)
const labels = ['Monday' ,'Tuesday' ,'Wednesday' ,'Thursday' ,'Friday' ,'Saturday' ,'Sunday'];
export const WeeklyCheckoutsChart = () => {
    const theme = useMantineTheme()
    return (
        <Bar
            options={{
                responsive: true,
                plugins: {
                    legend: {
                        display: true
                    },
                },
                scales: {
                    y: {
                        type: 'linear',
                        display: true,
                        position: 'left',
                    },
                    y1: {
                        type: 'linear',
                        display: true,
                        position: 'right',
                        grid: {
                            drawOnChartArea: false,
                        },
                    },
                }
            }}
            data={{
                labels,
                datasets: [
                    {
                        label: 'Daily checkouts',
                        data: data.dailyCheckouts.map((checkout) => checkout.numberOfCheckouts),
                        backgroundColor: theme.colors.teal[5],
                        yAxisID: 'y'
                    },
                    {
                        label: 'Average spending',
                        data: data.dailyCheckouts.map((checkout) => checkout.avgSpending),
                        backgroundColor: theme.colors.indigo[5],
                        yAxisID: 'y1'
                    },
                ],

            }}
        />
    )
}

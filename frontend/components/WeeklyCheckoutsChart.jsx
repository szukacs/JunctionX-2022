// @ts-check

import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from 'chart.js'
import { Bar } from 'react-chartjs-2'

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend)

const labels = ['January', 'February', 'March', 'April', 'May', 'June', 'July']

const data = {
    from: '2020-06-06',
    until: '2020-06-13',
    weeks: 1,
    dailyCheckouts: [
        {
            dayOfWeek: 1,
            numberOfCheckouts: 294,
        },
        {
            dayOfWeek: 2,
            numberOfCheckouts: 608,
        },
        {
            dayOfWeek: 3,
            numberOfCheckouts: 260,
        },
        {
            dayOfWeek: 4,
            numberOfCheckouts: 238,
        },
        {
            dayOfWeek: 5,
            numberOfCheckouts: 1343,
        },
        {
            dayOfWeek: 6,
            numberOfCheckouts: 4383,
        },
        {
            dayOfWeek: 7,
            numberOfCheckouts: 227,
        },
    ],
}

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

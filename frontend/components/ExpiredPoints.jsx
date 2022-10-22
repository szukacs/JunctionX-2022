import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend,
    PointElement,
    LineElement, Filler, TimeScale
} from 'chart.js'
import {Bar} from 'react-chartjs-2'
import 'chartjs-adapter-moment'
import data from '../src/data/pointsExpireAt.json'
import { useMantineTheme } from '@mantine/core'
import { transparentize } from 'polished'

ChartJS.register(CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Filler,
    Legend, TimeScale)

export const ExpiredPoints = ({min, max}) => {
    const theme = useMantineTheme()

    return (
        <Bar
            options={{
                responsive: true,
                fill: true,
                plugins: {
                    legend: {
                        position: 'top',
                    },
                },
                scales: {
                    x: {
                        type: "time",
                        min,
                        max
                    },
                },
            }}
            data={{
                labels: data.map((expiration) => expiration.date),
                datasets: [
                    {
                        label: 'Expired Points',
                        data: data.map((expiration) => expiration.expiredPoints),
                        yAxisID: 'y1',
                        borderColor: theme.colors.teal[5],
                        backgroundColor: theme.colors.teal[3]
                    },
                ],
            }}
        />
    )
}
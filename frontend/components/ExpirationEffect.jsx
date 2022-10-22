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
import {Line} from 'react-chartjs-2'
import 'chartjs-adapter-moment'
import dailyCheckouts from '../src/data/dailyCheckoutsAndAwardedPoints.json'
import dailyExpiration from '../src/data/pointsExpireAt.json'
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

export const ExpirationEffect = ({min, max}) => {
    const theme = useMantineTheme()

    return (
        <Line
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
                        max,
                    },
                },
            }}
            data={{
                labels: [...dailyCheckouts.map((checkout) => checkout.date), ...dailyExpiration.map((expiration) => expiration.date)],
                datasets: [
                    {
                        label: 'Daily checkout number',
                        data: dailyCheckouts.map((checkout) => checkout.count),
                        borderColor: theme.colors.indigo[5],
                        backgroundColor: transparentize(0.5, theme.colors.indigo[3])
                    },
                    {
                        label: 'Daily number of expiration',
                        data: dailyExpiration.map((expiration) => expiration.numberOfExpires),
                        borderColor: theme.colors.teal[5],
                        backgroundColor: transparentize(0.5, theme.colors.teal[3])
                    },
                ],
            }}
        />
    )
}
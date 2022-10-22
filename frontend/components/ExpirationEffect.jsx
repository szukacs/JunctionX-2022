import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend,
    PointElement,
    LineElement,
    Filler,
    TimeScale,
} from 'chart.js'
import { Line } from 'react-chartjs-2'
import 'chartjs-adapter-moment'
import dailyCheckouts from '../src/data/dailyCheckoutsAndAwardedPoints.json'
import dailyExpiration from '../src/data/pointsExpireAt.json'
import { useMantineTheme } from '@mantine/core'
import { transparentize } from 'polished'
import annotationPlugin from 'chartjs-plugin-annotation'

ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Filler,
    annotationPlugin,
    Legend,
    TimeScale
)

export const ExpirationEffect = ({ min, max }) => {
    const theme = useMantineTheme()

    return (
        <Line
            options={{
                responsive: true,
                fill: true,
                plugins: {
                    legend: {
                        display: false,
                    },
                    annotation: {
                        annotations: dailyExpiration.map((exp, index) => ({
                            type: 'line',
                            xMin: exp.date,
                            xMax: exp.date,
                            backgroundColor: 'rgba(255, 99, 132, 0.25)',
                        })),
                    },
                },
                scales: {
                    x: {
                        type: 'time',
                        min,
                        max,
                    },
                },
            }}
            data={{
                labels: [
                    ...dailyCheckouts.map((checkout) => checkout.date),
                ],
                datasets: [
                    {
                        label: 'Daily checkout number',
                        data: dailyCheckouts.map((checkout) => checkout.count),
                        borderColor: theme.colors.indigo[5],
                        backgroundColor: transparentize(0.5, theme.colors.indigo[3]),
                    },
                ],
            }}
        />
    )
}

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
    ArcElement
} from 'chart.js'
import { Doughnut } from 'react-chartjs-2'
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
    ArcElement,
    Title,
    Tooltip,
    Filler,
    Legend,
    TimeScale
)

export const Donut = ({ min, max }) => {
    const theme = useMantineTheme()

    return (
        <Doughnut
            options={{
                responsive: true,
                fill: true,
                plugins: {
                    legend: {
                        display: false,
                    },
                },
            }}
            data={{
                labels: [
                    'A', 'B', 'C', 'D'
                ],
                datasets: [
                    {
                        label: 'Daily checkout number',
                        data: [1, 2, 3, 4],
                        backgroundColor: [
                            theme.colors.indigo[3],
                            theme.colors.cyan[3],
                            theme.colors.pink[3],
                            theme.colors.orange[3],
                        ],
                    },
                ],
            }}
        />
    )
}

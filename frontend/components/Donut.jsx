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
import data from '../src/data/activitiesByDay.json'
import moment from 'moment/moment'

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

const normalizedData = Object.entries(data).map(([key, value]) => {
    return {
        date: key,
        ...value
    }
})

export const Donut = ({ min, max }) => {
    const theme = useMantineTheme()

    const filteredData = normalizedData.filter(data => {
        if (!min && !max) {
            return true
        }
        return moment(data.date).isBetween(min, max)
    })

    const createReducedData = (field) => {
        return filteredData.reduce((acc, curr) => {
            if (curr[field]) {
                acc = acc + curr[field]
            }
            return acc
        }, 0)
    }

    return (
        <Doughnut
            options={{
                responsive: true,
                fill: true,
                plugins: {
                    legend: {
                        display: true,
                    },
                },
            }}
            data={{
                labels: ['Reward', 'Checkout', 'Level up', 'Coupon redeem', 'Profile'],
                datasets: [
                    {
                        label: 'Event count by action',
                        data: [
                            createReducedData('reward'),
                            createReducedData('checkout'),
                            createReducedData('level_up'),
                            createReducedData('cupon_redeem'),
                            createReducedData('profile'),
                        ],
                        backgroundColor: [
                            theme.colors.indigo[3],
                            theme.colors.cyan[3],
                            theme.colors.pink[3],
                            theme.colors.orange[3],
                            theme.colors.blue[3],
                            theme.colors.lime[3],
                            theme.colors.violet[3],
                        ],
                    },
                ],
            }}
        />
    )
}

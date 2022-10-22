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
import grantedPointsData from '../src/data/pointsGrantedAt.json'
import activitiesData from '../src/data/activitiesAt.json'
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

export const sortByDate = (a, b) => {
    if (a.date < b.date) {
        return 1;
    }
    if (a.date > b.date) {
        return -1;
    }
    return 0;
}

export const ActivityPercentage = ({min, max}) => {
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
                labels: [...grantedPointsData.map((grantedPoints) => grantedPoints.date), ...activitiesData.map((expiredPoints) => expiredPoints.date)],
                datasets: [
                    {
                        label: 'Points Rewarded for Activity',
                        data: activitiesData.map((expiration) => expiration.rewardedPoints),
                        borderColor: theme.colors.indigo[5],
                        backgroundColor: transparentize(0.5, theme.colors.indigo[3])
                    },
                    {
                        label: 'Granted Points',
                        data: grantedPointsData.map((expiration) => expiration.grantedPoints),
                        borderColor: theme.colors.teal[5],
                        backgroundColor: transparentize(0.5, theme.colors.teal[3])
                    },
                ],
            }}
        />
    )
}


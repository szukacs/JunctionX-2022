import { Box, Button, Card, SimpleGrid, Stack, Text, Title } from '@mantine/core'
import { ExpiredPoints } from '../../../components/ExpiredPoints'
import GrantedPoints from '../../../components/GrantedPoints'
import { WeeklyCheckoutsChart } from '../../../components/WeeklyCheckoutsChart'

const ChartsPage = () => {
    return (
        <Box p={16}>
            <SimpleGrid cols={2}>
                <Card shadow="sm" p="lg" radius="md" withBorder>
                    <Stack>
                        <Title align='center' order={3}>Expired Points</Title>
                        <ExpiredPoints />
                    </Stack>
                </Card>
                <Card shadow="sm" p="lg" radius="md" withBorder>
                    <Stack>
                        <Title align='center' order={3}>Weekly Checkouts</Title>
                        <WeeklyCheckoutsChart />
                    </Stack>
                </Card>
                <Card shadow="sm" p="lg" radius="md" withBorder>
                    <Stack>
                        <Title align='center' order={3}>Granted Points</Title>
                        <GrantedPoints />
                    </Stack>
                </Card>
            </SimpleGrid>
        </Box>
    )
}

export default ChartsPage

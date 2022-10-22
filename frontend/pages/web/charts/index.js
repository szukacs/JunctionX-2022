import { Box, Button, Card, Group, SimpleGrid, Stack, Text, Title } from '@mantine/core'
import { ActivityPercentage } from '../../../components/ActivityPercentage'
import { ExpiredPoints } from '../../../components/ExpiredPoints'
import { GrantedPoints } from '../../../components/GrantedPoints'
import { WeeklyCheckoutsChart } from '../../../components/WeeklyCheckoutsChart'
import { RewardClaim } from '../../../components/RewardClaim'
import { DatePicker, DateRangePickerValue } from '@mantine/dates'
import { useState } from 'react'
import { ExpirationEffect } from '../../../components/ExpirationEffect'

const ChartsPage = () => {
    const [startDate, setStartDate] = useState(undefined)
    const [endDate, setEndDate] = useState(undefined)

    return (
        <Box p={16}>
            <Stack>
                <Box sx={{ position: 'sticky', top: 0, zIndex: 1, backgroundColor: '#fff', paddingBottom: 8 }}>
                    <Group>
                        <DatePicker label="From" value={startDate} onChange={setStartDate} />
                        <DatePicker label="To" value={endDate} onChange={setEndDate} />
                    </Group>
                </Box>
                <SimpleGrid cols={2}>
                    <Card shadow="sm" p="lg" radius="md" withBorder>
                        <Stack>
                            <Title align="center" order={3}>
                                Expired Points
                            </Title>
                            <ExpiredPoints min={startDate} max={endDate} />
                        </Stack>
                    </Card>
                    <Card shadow="sm" p="lg" radius="md" withBorder>
                        <Stack>
                            <Title align="center" order={3}>
                                Weekly Checkouts
                            </Title>
                            <WeeklyCheckoutsChart />
                        </Stack>
                    </Card>
                    <Card shadow="sm" p="lg" radius="md" withBorder>
                        <Stack>
                            <Title align="center" order={3}>
                                Average points / claim
                            </Title>
                            <RewardClaim min={startDate} max={endDate} />
                        </Stack>
                    </Card>
                    <Card shadow="sm" p="lg" radius="md" withBorder>
                        <Stack>
                            <Title align="center" order={3}>
                                Activity Percentage
                            </Title>
                            <ActivityPercentage min={startDate} max={endDate} />
                        </Stack>
                    </Card>
                    <Card shadow="sm" p="lg" radius="md" withBorder>
                        <Stack>
                            <div>
                                <Title align="center" order={3}>
                                    Effect of expiration on daily checkouts
                                </Title>
                                <Text align="center">Vertical lines represent expirations</Text>
                            </div>
                            <ExpirationEffect min={startDate} max={endDate} />
                        </Stack>
                    </Card>
                </SimpleGrid>
            </Stack>
        </Box>
    )
}

export default ChartsPage

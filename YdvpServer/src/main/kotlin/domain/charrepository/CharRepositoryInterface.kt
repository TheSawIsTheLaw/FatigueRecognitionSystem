package domain.charrepository

import domain.logicentities.DSDataAccessInfo
import domain.logicentities.DSDataAddInfo
import domain.logicentities.DSMeasurement

interface CharRepositoryInterface
{
    fun get(dataAccessInfo: DSDataAccessInfo): List<DSMeasurement>

    fun add(dataAddInfo: DSDataAddInfo)
}
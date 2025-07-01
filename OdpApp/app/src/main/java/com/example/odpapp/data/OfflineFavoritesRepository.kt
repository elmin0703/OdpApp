package com.example.odpapp.data

import kotlinx.coroutines.flow.Flow

class OfflineFavoritesRepository(
    private val favoriteResidenceDao: FavoriteResidenceDao,
    private val favoriteVehicleDao: FavoriteVehicleDao
) : FavoritesRepository {

    override fun getAllFavoriteResidences(): Flow<List<FavoriteResidenceEntity>> = favoriteResidenceDao.getAll()
    override fun getAllFavoriteVehicles(): Flow<List<FavoriteVehicleEntity>> = favoriteVehicleDao.getAll()
    override fun isResidenceFavorite(id: Int): Flow<Boolean> = favoriteResidenceDao.isFavorite(id)
    override fun isVehicleFavorite(id: Int): Flow<Boolean> = favoriteVehicleDao.isFavorite(id)

    override suspend fun addResidenceToFavorites(residence: ResidenceEntity) {
        val favorite = FavoriteResidenceEntity(
            id = residence.id,
            entitet = residence.entitet,
            canton = residence.canton,
            municipality = residence.municipality,
            institution = residence.institution,
            dateUpdate = residence.dateUpdate,
            saTrajPreb = residence.saTrajPreb,
            saPrivPreb = residence.saPrivPreb,
            total = residence.total
        )
        favoriteResidenceDao.insert(favorite)
    }

    override suspend fun removeResidenceFromFavorites(id: Int) {
        favoriteResidenceDao.deleteById(id)
    }

    override suspend fun addVehicleToFavorites(vehicle: VehicleEntity) {
        val favorite = FavoriteVehicleEntity(
            id = vehicle.id,
            entity = vehicle.entity,
            canton = vehicle.canton,
            municipality = vehicle.municipality,
            year = vehicle.year,
            month = vehicle.month,
            dateUpdate = vehicle.dateUpdate,
            mjestoReg = vehicle.mjestoReg,
            domVoz = vehicle.domVoz,
            strVoz = vehicle.strVoz,
            total = vehicle.total
        )
        favoriteVehicleDao.insert(favorite)
    }

    override suspend fun removeVehicleFromFavorites(id: Int) {
        favoriteVehicleDao.deleteById(id)
    }

    override fun getFavoriteResidenceById(id: Int): Flow<FavoriteResidenceEntity?> = favoriteResidenceDao.getById(id)

    override fun getFavoriteVehicleById(id: Int): Flow<FavoriteVehicleEntity?> = favoriteVehicleDao.getById(id)
}
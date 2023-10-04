import React, { useState } from 'react'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import * as S from '../../styles/refrigerator/Refrigerator.styled'
import CookForMe from '../../components/refrigerator/CookForMe'
import CookForYou from '../../components/refrigerator/CookForYou'

const RefrigeratorPage = () => {
  const [userType, setUserType] = useState('requester')
  return (
    <>
      <Header />
      <S.Container>
        <S.Map />
        <S.SelectUserList>
          <S.SelectTypeButton
            type="button"
            onClick={() => setUserType('requester')}
          >
            요청자
          </S.SelectTypeButton>
          <S.SelectTypeButton
            type="button"
            onClick={() => setUserType('cooker')}
          >
            요리사
          </S.SelectTypeButton>
        </S.SelectUserList>
        {userType === 'requester' ? <CookForMe /> : <CookForYou />}
      </S.Container>
      <BottomNavigation />
    </>
  )
}

export default RefrigeratorPage

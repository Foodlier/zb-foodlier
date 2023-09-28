import React from 'react'
import styled from 'styled-components'
import * as S from '../../styles/search/MoSearch.styled'
import SearchBar from './SearchBar'
import useIcon from '../../hooks/useIcon'
import { breakpoints, palette } from '../../constants/Styles'

interface MoSearchProps {
  setIsMoSearchOpen: (isOpen: boolean) => void
}

const MoSearch: React.FC<MoSearchProps> = ({ setIsMoSearchOpen }) => {
  const { IcPrevLight } = useIcon()

  const handleSearch = (query: string) => {
    console.log(`MoSearch에서 검색: ${query}`)
  }

  const closeMoSearch = () => {
    setIsMoSearchOpen(false)
  }

  return (
    <S.MoSearchContainer>
      <S.SearchHeader>
        <S.CloseButton type="button" onClick={closeMoSearch}>
          <IcPrevLight size={5} color={palette.textPrimary} />
        </S.CloseButton>
        <S.SearchHeaderTxt>
          <p>검색</p>
        </S.SearchHeaderTxt>
      </S.SearchHeader>
      <SearchBar onSearch={handleSearch} />
    </S.MoSearchContainer>
  )
}

export default MoSearch

import React from 'react'
import styled from 'styled-components'
import SearchBar from './SearchBar'
import useIcon from '../../hooks/useIcon'
import { breakpoints, palette } from '../../constants/Styles'

interface MoSearchProps {
  setIsMoSearchOpen: (isOpen: boolean) => void
}

const MoSearchContainer = styled.div`
  position: fixed;
  display: flex;
  flex-direction: column;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: ${palette.white};
  z-index: 100;
  overflow-y: scroll;

  ${breakpoints.large} {
    display: none;
  }
`

const SearchHeader = styled.section`
  display: flex;
  width: 100%;
  height: 6rem;
  box-shadow: 0.1rem 0.5rem 1.1rem ${palette.shadow};
  margin-bottom: 5rem;
`

const CloseButton = styled.button`
  padding: 0rem 2rem;
  border: none;
  cursor: pointer;
`

const SearchHeaderTxt = styled.div`
  display: flex;
  position: relative;
  left: 50%;
  transform: translate(-25%);
  width: 100%;
  align-items: center;
  font-size: 2rem;

  & > p {
    font-weight: 600;
  }
`

const MoSearch: React.FC<MoSearchProps> = ({ setIsMoSearchOpen }) => {
  const { IcPrevLight } = useIcon()
  const handleSearch = (query: string) => {
    console.log(`MoSearch에서 검색: ${query}`)
  }

  const closeMoSearch = () => {
    setIsMoSearchOpen(false)
  }

  return (
    <MoSearchContainer>
      <SearchHeader>
        <CloseButton type="button" onClick={closeMoSearch}>
          <IcPrevLight size={5} color={palette.textPrimary} />
        </CloseButton>
        <SearchHeaderTxt>
          <p>검색</p>
        </SearchHeaderTxt>
      </SearchHeader>
      <SearchBar onSearch={handleSearch} />
    </MoSearchContainer>
  )
}

export default MoSearch

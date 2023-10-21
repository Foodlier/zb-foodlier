import React from 'react'

import * as S from '../../styles/search/SearchBar.styled'
import useIcon from '../../hooks/useIcon'
import { palette } from '../../constants/Styles'
import CustomSelect from '../ui/CustomSelect'
import { RECIPE_TYPE_MENU_LIST } from '../../constants/Data'
import { CustomSelectData } from '../../constants/Interfaces'

interface SearchBarProps {
  onClickSearchButton: () => void
  typeSelectValue: CustomSelectData
  setTypeSelectValue: (value: CustomSelectData) => void
  searchValue: string
  setSearchValue: (value: string) => void
}

function SearchBar({
  onClickSearchButton,
  typeSelectValue,
  setTypeSelectValue,
  searchValue,
  setSearchValue,
}: SearchBarProps) {
  const { IcSearch } = useIcon()

  const handleKeyPress = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      e.preventDefault()
      onClickSearchButton()
    }
  }

  return (
    <S.Container>
      <CustomSelect
        data={RECIPE_TYPE_MENU_LIST}
        setCurrentSelectValue={setTypeSelectValue}
        currentSelectValue={typeSelectValue}
      />
      <S.SearchForm>
        <S.SearchInput
          type="text"
          placeholder="검색"
          value={searchValue}
          onChange={e => setSearchValue(e.target.value)}
          onKeyDown={handleKeyPress}
        />
        <S.SearchButton type="button" onClick={onClickSearchButton}>
          <IcSearch size={3} color={palette.textPrimary} />
        </S.SearchButton>
      </S.SearchForm>
    </S.Container>
  )
}

export default SearchBar

import React, { useState } from 'react'
import styled from 'styled-components'
import useIcon from '../../hooks/useIcon'
import { breakpoints, palette } from '../../constants/Styles'

interface SearchBarProps {
  onSearch: (query: string) => void
}

const SearchForm = styled.form`
  position: relative;
  display: flex;
  align-items: center;
  padding: 0 5%;
  margin: 1rem 2rem 0 2rem;

  ${breakpoints.large} {
    margin: 3rem 0 6rem;
    padding: 2.6%;
    border-radius: 1rem;
  }
`

const SearchInput = styled.input`
  position: absolute;
  left: 0;
  width: 100%;
  flex: 1;
  outline: 0;
  border: 0.1rem solid ${palette.divider};
  border-radius: 1rem;
  padding: 1.8rem 2rem;
  box-shadow: 0.1rem 0.5rem 1.1rem ${palette.shadow};
`

const SearchButton = styled.button`
  position: absolute;
  right: 2rem;
`

const SearchBar: React.FC<SearchBarProps> = ({ onSearch }) => {
  const { IcSearch } = useIcon()
  const [text, setText] = useState('')

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    onSearch(text)
  }

  return (
    <SearchForm onSubmit={handleSubmit}>
      <SearchInput
        type="text"
        placeholder="Search"
        value={text}
        onChange={e => setText(e.target.value)}
      />
      <SearchButton type="submit">
        <IcSearch size={3} color={palette.textPrimary} />
      </SearchButton>
    </SearchForm>
  )
}

export default SearchBar

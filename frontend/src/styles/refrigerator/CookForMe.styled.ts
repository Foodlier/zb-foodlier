import styled from 'styled-components'
import { breakpoints, palette } from '../../constants/Styles'

export const Container = styled.div`
  padding: 2rem 2rem 10rem;
`

export const Info = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 2rem;
`
export const SubTitle = styled.span`
  color: ${palette.textPrimary};
  font-weight: 800;
  font-size: 2rem;
`

export const SelectBox = styled.div`
  width: 15rem;
  border: 1px solid ${palette.divider};
  border-radius: 1rem;
  padding: 0.5rem 1rem;
  text-align: left;
`

export const SelectedBox = styled.div`
  position: relative;
  font-weight: bold;
  font-size: 2rem;
  color: ${palette.textSecondary};
`

export const OptionList = styled.ul<{ $toggle: boolean }>`
  display: ${props => (props.$toggle ? 'block' : 'none')};
  width: 100%;
  position: absolute;
  top: 30px;
  left: 0;
  background-color: ${palette.white};
  border: 1px solid ${palette.divider};
  padding-left: 0;
  border-radius: 5px;
`

export const Option = styled.li`
  &:hover {
    background-color: ${palette.divider};
  }
`

export const OptionButton = styled.button`
  display: flex;
  justify-content: space-between;
  width: 100%;
  height: 100%;
  text-align: right;
  padding: 0 4px;
`

export const CardList = styled.ul`
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 500px;
  overflow: auto;
  padding-left: 0;
  &::-webkit-scrollbar {
    display: none;
  }
`

export const WritingButton = styled.button`
  width: 146px;
  height: 44px;
  line-height: 44px;
  color: ${palette.main};
  border-radius: 5px;
  font-weight: 800;
`

export const ButtonList = styled.div`
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  /* background-color: ${palette.white}; */
  background-color: ${palette.white};
  display: flex;
  justify-content: flex-end;
  /* gap: 10px; */
  border-top: 1px solid #d9d9d9d9;
  padding: 2rem;

  ${breakpoints.large} {
    /* justify-content: end; */
    /* padding: 10px 20px 0 0; */
  }
  button {
    margin-bottom: 110px;
    ${breakpoints.large} {
      margin-bottom: 10px;
    }
  }
`
